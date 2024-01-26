package com.kolya.gym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Arrays.asList(bearerSecurityContext()))
                .securitySchemes(Arrays.asList(bearerAuthScheme()));
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .docExpansion(DocExpansion.NONE)
                .operationsSorter(OperationsSorter.ALPHA)
                .defaultModelRendering(ModelRendering.MODEL)
                .build();
    }


    private SecurityContext bearerSecurityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(bearerAuthReference()))
                .forPaths(PathSelectors.any())  // for endpoints using JWT
                .build();
    }

    private SecurityReference bearerAuthReference() {
        return new SecurityReference("bearerAuth", new AuthorizationScope[0]);
    }

    private SecurityScheme bearerAuthScheme() {
        return new ApiKey("bearerAuth", "Authorization", "header");
    }
}