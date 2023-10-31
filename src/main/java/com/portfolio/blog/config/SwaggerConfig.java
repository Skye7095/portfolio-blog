package com.portfolio.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {
	
	@Bean
    public OpenAPI api() {
        Info info = new Info()
        		.title("Blog")
        		.version("v1")
        		.description("Blog's Endpoint");

        return new OpenAPI().components(new Components()).info(info);
    }
}
