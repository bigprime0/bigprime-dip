package com.bigprime.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author lyw
 * @version 1.0
 */
@Configuration
public class RestClientConfig {

    @Bean
    @LoadBalanced // 启用负载均衡
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
