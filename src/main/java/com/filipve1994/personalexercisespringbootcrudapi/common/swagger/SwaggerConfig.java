package com.filipve1994.personalexercisespringbootcrudapi.common.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String DB_SERVICES_TAG = "DB services";
    public static final String EXTERNAL_SERVICES_JSON_TAG = "External services JSON";
    public static final String EXTERNAL_SERVICES_TAG = "External services";

    /**
     * By using RestController.class, we only show all controllers that are annotated with @RestController.
     *
     * @param swaggerConfigProperties - class that reads all swagger properties from a config file
     * @return Docket - A builder which is intended to be the primary interface into the Springfox framework.
     */
    @Bean
    public Docket api(SwaggerConfigProperties swaggerConfigProperties) {

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(Boolean.valueOf(swaggerConfigProperties.getEnabled()))
                //.useDefaultResponseMessages(true)
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, overrideGlobalResponseMessages())
                .globalResponseMessage(RequestMethod.PUT, overrideGlobalResponseMessages())
                .globalResponseMessage(RequestMethod.GET, overrideGlobalResponseMessages())
                .globalResponseMessage(RequestMethod.DELETE, overrideGlobalResponseMessages())
                // .globalOperationParameters(getGlobalOperationParameters())
//                .tags(
//                        new Tag("External services", "All controllers that uses external services"),
//                        new Tag("External services JSON", "All controllers that uses external services for the json version"),
//                        new Tag(DB_SERVICES_TAG, "All controllers that are connected directly to a database")
//                )
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo(swaggerConfigProperties))
                .pathMapping("/")

                //
                .directModelSubstitute(LocalDate.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                //

                ;
    }

    @Value("${application.version:unknown}")
    private String applicationVersion;

    private ApiInfo apiInfo(SwaggerConfigProperties swaggerConfigProperties) {

        return new ApiInfoBuilder()
                .title(swaggerConfigProperties.getTitle())
                .description(swaggerConfigProperties.getDescription())
                .contact(new Contact(swaggerConfigProperties.getContactname(), null, null))
                .license(swaggerConfigProperties.getLicense())
                .licenseUrl("")
                .version(applicationVersion)
                .build();
    }

    @Bean
    public UiConfiguration uiConfiguration(SwaggerConfigProperties swaggerConfigProperties) {
        return UiConfigurationBuilder.builder()
                .deepLinking(Boolean.valueOf(swaggerConfigProperties.getDeepLinking()))
                .validatorUrl(null)
                //
                .displayOperationId(Boolean.valueOf(swaggerConfigProperties.getDisplayOperationId()))
                .defaultModelsExpandDepth(Integer.valueOf(swaggerConfigProperties.getDefaultModelsExpandDepth()))
                .defaultModelExpandDepth(Integer.valueOf(swaggerConfigProperties.getDefaultModelExpandDepth()))
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(Boolean.valueOf(swaggerConfigProperties.getDisplayRequestDuration()))
                .docExpansion(DocExpansion.NONE)
                //.docExpansion(DocExpansion.FULL)
                //.docExpansion(DocExpansion.LIST)

                .filter(Boolean.valueOf(swaggerConfigProperties.getFilter()))
                .maxDisplayedTags(Integer.valueOf(swaggerConfigProperties.getMaxDisplayedTags()))
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(Boolean.valueOf(swaggerConfigProperties.getShowExtensions()))
                .tagsSorter(TagsSorter.ALPHA)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)

                //
                .build();
    }

//    private List<Parameter> getGlobalOperationParameters() {
//
//        List<Parameter> parameters = new ArrayList<>();
//
//        Parameter pmbRequestHeader1 = new ParameterBuilder()
//                .parameterType("header")
//                .name(CORRELATION_ID_HEADER_NAME)
//                .description("To follow the flow in the logfiles")
//                .modelRef(new ModelRef("string"))
//                .required(false)
//                .build();
//
//        parameters.add(pmbRequestHeader1);
//
//        return parameters;
//
//    }

    private List<ResponseMessage> overrideGlobalResponseMessages() {

        return Arrays.asList(
                new ResponseMessageBuilder().code(HttpStatus.OK.value()).message("Success").build(),
                new ResponseMessageBuilder().code(HttpStatus.CREATED.value()).message(HttpStatus.CREATED.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value()).message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value()).message(HttpStatus.FORBIDDEN.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.NOT_FOUND.value()).message(HttpStatus.NOT_FOUND.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Server has crashed!!!").build()
        );

    }
}
