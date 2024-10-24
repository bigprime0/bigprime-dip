package com.bigprime.controller.integration;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bigprime.common.base.Result;
import com.bigprime.handler.integration.model.IntegrationJobLogModel;
import com.bigprime.service.integration.IntegrationJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api")
@Tag(name = "数据集成配置")
@AllArgsConstructor
public class IntegrationWebHookController {
    
    private final IntegrationJobService integrationJobService;

    //seatunnel的eventLog
    @PostMapping("/seatunnel/event/report")
    @Operation(summary = "seatunnel任务进度上报")
    public Result<String> getSeatunnelJobEventLog(@RequestBody @Valid JSONArray report) {
        System.out.println(report);
        return Result.ok("ok");
    }

    //addax事件上报
    @PostMapping("/addax/event/report")
    @Operation(summary = "seatunnel任务进度上报")
    public Result<String> getAddaxJobEventLog(@RequestBody @Valid JSONObject report) {
        System.out.println(report);
        IntegrationJobLogModel logModel = integrationJobService.getJobLog(report.get("jobName").toString());
        if (logModel != null) {
            logModel.setMessage(JSONUtil.toJsonStr(report, 4));
            integrationJobService.updateJobLog(logModel);
        }
        return Result.ok();
    }
}
