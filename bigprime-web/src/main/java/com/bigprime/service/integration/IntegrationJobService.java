package com.bigprime.service.integration;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bigprime.common.base.MyPageResult;
import com.bigprime.common.config.NacosConfig;
import com.bigprime.common.utils.JsonUtils;
import com.bigprime.common.utils.StrUtils;
import com.bigprime.handler.database.model.DataDatabaseModel;
import com.bigprime.handler.integration.IntegrationJobHandler;
import com.bigprime.handler.integration.model.IntegrationJobLogModel;
import com.bigprime.handler.integration.model.IntegrationJobModel;
import com.bigprime.query.integration.IntegrationJobLogQuery;
import com.bigprime.query.integration.IntegrationJobQuery;
import com.bigprime.source.spi.model.SourceConfig;
import com.bigprime.vo.integration.IntegrationJobVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static cn.hutool.core.io.FileUtil.writeString;

@Slf4j
@Service
@AllArgsConstructor
public class IntegrationJobService {
    private final IntegrationJobHandler jobHandler;
    private final NacosConfig config;

    public MyPageResult<IntegrationJobModel> getJobPageList(IntegrationJobQuery query) {
        return jobHandler.getJobPageList(query.getModel(), query.getPage(), query.getLimit());
    }

    public boolean saveJob(IntegrationJobModel job) {
        return jobHandler.saveJob(job);
    }

    public boolean deleteJob(long id) {
        return jobHandler.deleteJob(id);
    }

    public IntegrationJobVO submitJob(IntegrationJobModel job) {
        IntegrationJobVO vo = new IntegrationJobVO();
        IntegrationJobLogModel logModel = new IntegrationJobLogModel();
        logModel.setJobDefinedId(job.id);
        logModel.setEngine(job.getEngine());
        logModel.setType(job.getType() == null ? 0 : job.getType());
        logModel.setContent(job.getContent());
        logModel.setFormDatas(job.getFormDatas());
        logModel.setJobName(job.getJobName());
        logModel.setWriteSourceId(job.getWriteSourceId());
        logModel.setWriteSourceTable(job.getWriteSourceTable());
        if (job.getEngine().equalsIgnoreCase("seatunnel")) {
            //保存后同时调用submitJob
            String result = HttpUtil.post(job.getUrl() + "/submit-job", job.getContent());
            JSONObject json = (JSONObject) JSONUtil.parse(result);
            logModel.setStartTime(new Date());
            if (json.containsKey("jobId")) {
                logModel.setJobId(json.get("jobId").toString());
                vo.setMessage(logModel.getJobId());
                if (StrUtils.IsNullOrEmpty(job.getJobName())) {
                    logModel.setJobName(json.get("jobName").toString());
                }
                logModel.setStatus("success");
            } else {
                if (json.containsKey("status")) {
                    logModel.setStatus(json.get("status").toString());
                }
                if (json.containsKey("message")) {
                    logModel.setMessage(json.get("message").toString());
                    vo.setMessage(logModel.getMessage());
                }
            }
            jobHandler.saveJobLog(logModel);
            vo.setStatus(logModel.getStatus());
        }

        //把job的json串定入Addax文件目录，使用cmd命令行运行
        if (job.getEngine().equalsIgnoreCase("addax")) {
            String homePath = job.getUrl();
            //生成临时的job文件名
            String jobId = StrUtils.GenNanoId();
            logModel.setJobId(jobId);
            CommandLine cmd = null;
            //生成job配置文件
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                homePath = homePath.endsWith("/") ? homePath : homePath + "/";
                writeString(job.getContent(), homePath.concat("tmp/").concat(jobId).concat(".json"), CharsetUtil.UTF_8);

                cmd = new CommandLine("cmd");
                cmd.addArgument("/c");
                cmd.addArgument("python");
                cmd.addArgument(homePath + "bin/addax.py");
                cmd.addArgument(homePath.concat("tmp/").concat(jobId).concat(".json"));
                cmd.addArgument("--params");
                cmd.addArgument("-DjobName=" + jobId);
            }

            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                homePath = homePath.endsWith("/") ? homePath : homePath + "/";
                writeString(job.getContent(), homePath.concat("tmp/").concat(jobId).concat(".json"), CharsetUtil.UTF_8);

                cmd = new CommandLine("sh");
                cmd.addArgument(homePath + "bin/addax.sh");
                cmd.addArgument(homePath.concat("tmp/").concat(jobId).concat(".json"));
                cmd.addArgument("--params");
                cmd.addArgument("-DjobName=" + jobId);
            }
            //调用CommandLine命令执行同步任务
            ByteArrayOutputStream susStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errStream = new ByteArrayOutputStream();
            DefaultExecutor exec = new DefaultExecutor();
            exec.setExitValue(0);
            PumpStreamHandler streamHandler = new PumpStreamHandler(susStream, errStream);
            exec.setStreamHandler(streamHandler);
            try {
                logModel.setStartTime(new Date());
                logModel.setStatus("running");
                jobHandler.saveJobLog(logModel);
                exec.execute(cmd, new ExecuteResultHandler() {
                    @Override
                    public void onProcessComplete(int exitValue) {
                        try {
                            logModel.setEndTime(new Date());
                            String suc = susStream.toString("GBK");
                            logModel.setStatus("success");
                            logModel.setLogs(suc);
                            IntegrationJobLogModel dbModel = jobHandler.getJobLogByJobId(logModel.getJobId());
                            if (StrUtils.IsNullOrEmpty(logModel.getMessage())) {
                                logModel.setMessage(dbModel.getMessage());
                            }
                            jobHandler.updateJobLog(logModel);
                        } catch (UnsupportedEncodingException uee) {
                            logModel.setStatus("exception");
                            logModel.setMessage(uee.getMessage());
                            jobHandler.updateJobLog(logModel);
                        }
                    }

                    @Override
                    public void onProcessFailed(ExecuteException e) {
                        try {
                            logModel.setEndTime(new Date());
                            String err = errStream.toString("GBK");
                            logModel.setStatus("error");
                            logModel.setMessage(e.getMessage());
                            logModel.setLogs(err);
                            jobHandler.updateJobLog(logModel);
                        } catch (UnsupportedEncodingException uee) {
                            logModel.setStatus("exception");
                            logModel.setMessage(uee.getMessage());
                            jobHandler.updateJobLog(logModel);
                        }
                    }
                });
                vo.setStatus("success");
                vo.setMessage("任务提交成功,jobId:" + jobId);
            } catch (IOException e) {
                logModel.setStatus("exception");
                logModel.setMessage(e.getMessage());
                logModel.setEndTime(new Date());
                jobHandler.updateJobLog(logModel);
                vo.setStatus("exception");
                vo.setMessage("任务提交失败:" + e.getMessage());
            }
        }

        return vo;
    }

    public MyPageResult<IntegrationJobLogModel> getJobLogPageList(IntegrationJobLogQuery query) {
        return jobHandler.getJobLogPageList(query.getModel(), query.getPage(), query.getLimit());
    }

    public IntegrationJobLogModel getJobLog(String jobId) {
        return jobHandler.getJobLogByJobId(jobId);
    }

    public Boolean updateJobLog(IntegrationJobLogModel jobLog) {
        return jobHandler.updateJobLog(jobLog);
    }

    /**
     * 构建主数据同步的写入配置
     * @param masterTable
     * @return
     */
    public Map<String, Object> buildMasterDataWriter(String masterTable) {
        Map<String, Object> writer = new HashMap<>();
        DataDatabaseModel dbModel = config.getDataBaseModel();
        SourceConfig param = JsonUtils.parseObject(dbModel.getConfig(), SourceConfig.class);
        //固定rdbmswriter结构
        writer.put("jdbcUrl", param.getUrl());
        writer.put("username", param.getUsername());
        writer.put("password", param.getPassword());
        writer.put("driver", "com.mysql.cj.jdbc.Driver");
        writer.put("name", "rdbmswriter");
        writer.put("table", masterTable);
        return writer;
    }

    @Transactional
    public IntegrationJobVO saveAndSubmitJob(IntegrationJobModel model) {
        IntegrationJobVO vo = new IntegrationJobVO();
        model.setType(1);
        boolean saveFlag = jobHandler.saveJob(model);
        if (saveFlag) {
            vo = submitJob(model);
        } else {
            vo.setStatus("error");
            vo.setMessage("保存同步任务失败");
        }
        return vo;
    }
}
