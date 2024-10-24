package com.bigprime.controller.integration;

import com.bigprime.common.base.MyPageResult;
import com.bigprime.common.base.Result;
import com.bigprime.handler.integration.model.IntegrationJobLogModel;
import com.bigprime.handler.integration.model.IntegrationJobModel;
import com.bigprime.query.integration.IntegrationJobLogQuery;
import com.bigprime.query.integration.IntegrationJobQuery;
import com.bigprime.service.integration.IntegrationJobService;
import com.bigprime.vo.integration.IntegrationJobVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("integration")
@Tag(name = "数据集成配置")
@AllArgsConstructor
public class IntegrationJobController {
    private final IntegrationJobService integrationJobService;

    @PostMapping("/job/page")
    @Operation(summary = "查询集成任务列表(分页)")
    public Result<MyPageResult<IntegrationJobModel>> getJobPage(@RequestBody @Valid IntegrationJobQuery query) {
        return Result.ok(integrationJobService.getJobPageList(query));
    }

    @PostMapping("/job/save")
    @Operation(summary = "保存集成任务")
    public Result<Boolean> saveJob(@RequestBody @Valid IntegrationJobModel model) {
        return Result.ok(integrationJobService.saveJob(model));
    }

    @DeleteMapping("/job/{id}")
    @Operation(summary = "根据id删除任务")
    public Result<Boolean> deleteJob(@PathVariable("id") Long id) {
        return Result.ok(integrationJobService.deleteJob(id));
    }

    @PostMapping("/job/submit")
    @Operation(summary = "提交任务")
    public Result<IntegrationJobVO> submitJob(@RequestBody @Valid IntegrationJobModel model) {
        return Result.ok(integrationJobService.submitJob(model));
    }

    @PostMapping("/job/save-submit")
    @Operation(summary = "提交任务")
    public Result<IntegrationJobVO> saveAndSubmitJob(@RequestBody @Valid IntegrationJobModel model) {
        return Result.ok(integrationJobService.saveAndSubmitJob(model));
    }

    @PostMapping("/job/log/page")
    @Operation(summary = "查询集成任务日志列表(分页)")
    public Result<MyPageResult<IntegrationJobLogModel>> getJobLogPage(@RequestBody @Valid IntegrationJobLogQuery query) {
        return Result.ok(integrationJobService.getJobLogPageList(query));
    }

    @PostMapping("/master/writer")
    @Operation(summary = "获取主数据同步Writer的默认配置")
    public Result<Map<String, Object>> getMasterDataWriter(@RequestBody @Valid Map<String, String> params) {
        return Result.ok(integrationJobService.buildMasterDataWriter(params.get("table").toString()));
    }
}
