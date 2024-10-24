package com.bigprime.common.config;

import com.bigprime.common.exception.SecurityAuthenticationEntryPoint;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author lyw
 * @version 1.0
 */
@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityFilterConfig {
    private final OncePerRequestFilter authenticationTokenFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 忽略授权的地址列表
        List<String> permitList = getPermitList();
        String[] permits = permitList.toArray(new String[0]);

        http
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(permits).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
                .and().headers().frameOptions().disable()
                .and().csrf().disable()
        ;

        return http.build();
    }

    @SneakyThrows
    private List<String> getPermitList(){
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:auth.yml");
        String key = "auth.ignore_urls";

        return getPropertiesList(key, resources);
    }

    private List<String> getPropertiesList(String key, Resource... resources){
        List<String> list = new ArrayList<>();

        // 解析资源文件
        for(Resource resource : resources) {
            Properties properties = loadYamlProperties(resource);

            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String tmpKey = StringUtils.substringBefore(entry.getKey().toString(), "[");
                if(tmpKey.equalsIgnoreCase(key)){
                    list.add(entry.getValue().toString());
                }
            }
        }
        return list;
    }

    private Properties loadYamlProperties(Resource... resources) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resources);
        factory.afterPropertiesSet();

        return factory.getObject();
    }
}
