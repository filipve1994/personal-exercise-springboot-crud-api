package com.filipve1994.personalexercisespringbootcrudapi.common.swagger;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Configuration
@PropertySource("classpath:properties/swagger.properties")
public class SwaggerConfigProperties {

    @Value("${api.version}")
    private String apiVersion;

    @Value("${swagger.enabled}")
    private String enabled = "false";

    @Value("${swagger.title}")
    private String title;

    @Value("${swagger.description}")
    private String description;

    @Value("${swagger.license}")
    private String license;

    @Value("${swagger.contactname}")
    private String contactname;

    @Value("${swagger.useDefaultResponseMessages}")
    private String useDefaultResponseMessages;

    @Value("${swagger.enableUrlTemplating}")
    private String enableUrlTemplating;

    @Value("${swagger.deepLinking}")
    private String deepLinking;

    @Value("${swagger.defaultModelsExpandDepth}")
    private String defaultModelsExpandDepth;

    @Value("${swagger.defaultModelExpandDepth}")
    private String defaultModelExpandDepth;

    @Value("${swagger.displayOperationId}")
    private String displayOperationId;

    @Value("${swagger.displayRequestDuration}")
    private String displayRequestDuration;

    @Value("${swagger.filter}")
    private String filter;

    @Value("${swagger.maxDisplayedTags}")
    private String maxDisplayedTags;

    @Value("${swagger.showExtensions}")
    private String showExtensions;

}
