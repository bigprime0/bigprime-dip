package com.bigprime.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lyw
 * @version 1.0
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi userApi(){
        String[] paths = { "/**" };
        String[] packagedToMatch = { "com.bigprime"};
        return GroupedOpenApi.builder().group("BigPrime")
                .pathsToMatch(paths)
                .packagesToScan(packagedToMatch).build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact= new Contact();

        OpenAPI openapi = new OpenAPI().info(new Info()
                .title("BigPrime")
                .description( "BigPrime")
                .contact(contact)
                .version("1.0")
                .license(new License().name("MIT")
                        .url("https://bigprime.com")));

        openapi.addSecurityItem(new SecurityRequirement().addList("api_key"))
                .components(new Components().addSecuritySchemes("api_key",
                        new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)));

        return openapi;
    }
}
