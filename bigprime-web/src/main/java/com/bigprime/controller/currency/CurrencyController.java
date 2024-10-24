package com.bigprime.controller.currency;

import com.bigprime.common.base.Result;
import com.bigprime.service.currency.CurrencyService;
import com.bigprime.vo.currency.StatisticVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lyw
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/currency")
@Tag(name = "通过接口")
@AllArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/get-home-statistic")
    @Operation(summary = "获取home统计")
    public Result<StatisticVO> getHomeStatistic() {
        return Result.ok(currencyService.getHomeStatistic());
    }

}
