package com.bigprime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * BigPrime
 * @author lyw
 * @version 1.0
 */
@EnableAsync
@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication
public class BigPrimeWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(BigPrimeWebApplication.class, args);
    }
}
