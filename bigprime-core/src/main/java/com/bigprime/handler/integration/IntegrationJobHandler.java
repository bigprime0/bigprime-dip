package com.bigprime.handler.integration;

import cn.hutool.core.util.StrUtil;
import com.bigprime.common.base.MyPageResult;
import com.bigprime.common.base.MyPager;
import com.bigprime.handler.integration.model.IntegrationJobLogModel;
import com.bigprime.handler.integration.model.IntegrationJobModel;
import com.bigprime.handler.integration.model.IntegrationJobStatistic;
import com.bigprime.handler.integration.model.proxy.IntegrationJobLogModelProxy;
import com.bigprime.handler.integration.model.proxy.IntegrationJobModelProxy;
import com.bigprime.handler.integration.model.proxy.IntegrationJobStatisticProxy;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import com.easy.query.core.proxy.sql.GroupKeys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class IntegrationJobHandler {
    private final EasyEntityQuery proxy;

    public MyPageResult<IntegrationJobModel> getJobPageList(IntegrationJobModel model, Integer page, Integer limit) {
        return proxy.queryable(IntegrationJobModel.class)
                .where(i -> {
                    extracted(model, i);
                }).toPageResult(new MyPager<>(page, limit));
    }

    public List<IntegrationJobModel> getJobList(IntegrationJobModel model) {
        return proxy.queryable(IntegrationJobModel.class)
                .where(i -> {
                    extracted(model, i);
                }).toList();
    }

    private static void extracted(IntegrationJobModel model, IntegrationJobModelProxy i) {
        if (model.getType() != null) {
            i.type().eq(model.getType());
        }
        if (model.getWriteSourceId() != null && model.getWriteSourceId() != 0) {
            i.writeSourceId().eq(model.getWriteSourceId());
        }
        if (StrUtil.isNotBlank(model.getWriteSourceTable())) {
            i.writeSourceTable().eq(model.getWriteSourceTable());
        }
        if (model.getReaderSourceId() != null && model.getReaderSourceId() != 0) {
            i.readerSourceId().eq(model.getReaderSourceId());
        }
        if (StrUtil.isNotBlank(model.getReaderSourceTable())) {
            i.readerSourceTable().eq(model.getReaderSourceTable());
        }
        if (StrUtil.isNotBlank(model.getEngine())) {
            i.engine().eq(model.getEngine());
        }
        if (StrUtil.isNotBlank(model.getJobName())) {
            i.jobName().like(model.getJobName());
        }
    }

    public boolean saveJob(IntegrationJobModel model) {
        if (model.getId() <= 0) {
            return proxy.insertable(model).executeRows(true) > 0;
        } else {
            return proxy.updatable(model).executeRows() > 0;
        }
    }

    public IntegrationJobModel getJobById(long id) {
        return proxy.queryable(IntegrationJobModel.class).whereById(id).firstOrNull();
    }

    public boolean deleteJob(long id) {
        return proxy.deletable(IntegrationJobModel.class).whereById(id).executeRows() > 0;
    }

    public MyPageResult<IntegrationJobLogModel> getJobLogPageList(IntegrationJobLogModel model, Integer page, Integer limit) {
        return proxy.queryable(IntegrationJobLogModel.class)
                .where(i -> {
                    if (model.getType() != null) {
                        i.type().eq(model.getType());
                    }

                    if (model.getWriteSourceId() != null && model.getWriteSourceId() > 0) {
                        i.writeSourceId().eq(model.getWriteSourceId());
                    }

                    if (StrUtil.isNotBlank(model.getWriteSourceTable())) {
                        i.writeSourceTable().eq(model.getWriteSourceTable());
                    }

                    if (model.getJobDefinedId() != null && model.getJobDefinedId() > 0) {
                        i.jobDefinedId().eq(model.getJobDefinedId());
                    } else if (model.getJobDefinedIds() != null && model.getJobDefinedIds().size() > 0) {
                        i.jobDefinedId().in(model.getJobDefinedIds());
                    }

                    if (StrUtil.isNotBlank(model.getJobName())) {
                        i.jobName().like(model.getJobName());
                    }
                })
                .orderBy(i -> i.startTime().desc())
                .toPageResult(new MyPager<>(page, limit));
    }

    public boolean saveJobLog(IntegrationJobLogModel model) {
        return proxy.insertable(model).executeRows() > 0;
    }

    public boolean updateJobLog(IntegrationJobLogModel model) {
        return proxy.updatable(model).executeRows() > 0;
    }

    public IntegrationJobLogModel getJobLogByJobId(String jobId) {
        return proxy.queryable(IntegrationJobLogModel.class)
                .where(i -> i.jobId().eq(jobId))
                .firstOrNull();
    }

    public List<IntegrationJobStatistic> getCount() {
        return proxy.queryable(IntegrationJobModel.class)
                .groupBy(i -> GroupKeys.TABLE1.of(i.type()))
                .select(g -> {
                    IntegrationJobStatisticProxy model = new IntegrationJobStatisticProxy();
                    model.type().set(g.key1());
                    model.count().set(g.intCount());
                    return model;
                }).toList();
    }

    public List<IntegrationJobStatistic> getJobLogCount() {
        return proxy.queryable(IntegrationJobLogModel.class)
                .groupBy(i -> GroupKeys.TABLE1.of(i.status(), i.type()))
                .select(g -> {
                    IntegrationJobStatisticProxy model = new IntegrationJobStatisticProxy();
                    model.status().set(g.key1());
                    model.type().set(g.key2());
                    model.count().set(g.intCount());
                    return model;
                }).toList();
    }
}
