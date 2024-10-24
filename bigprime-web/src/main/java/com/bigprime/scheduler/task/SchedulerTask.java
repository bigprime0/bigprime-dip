package com.bigprime.scheduler.task;

import com.bigprime.service.spi.SourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author lyw
 * @version 1.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SchedulerTask {
    private final SourceService sourceService;

    /**
     * 检测数据源
     */
    @Async("schedulerPoolExecutor")
    @Scheduled(cron = "0 0/30 * * * ?")
    public void detectingSources() {
        sourceService.detectingSources();
    }
}
