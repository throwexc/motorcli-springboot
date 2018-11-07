package com.motorcli.springboot.restful.utils;

import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalDate;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class DocketUtils {

    private final static String DOCKET_TYPE_DEFAULT = "default";

    private final static String DOCKET_TYPE_JWT = "JWT";

    public static Docket builder(String type, DocketInfo info) {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.groupName(info.getGroupName());

        docket
            .select()
            .apis(RequestHandlerSelectors.basePackage(info.getBasePackage()))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(info.getApiInfo())
            .pathMapping("/")
            .directModelSubstitute(LocalDate.class, String.class)
            .genericModelSubstitutes(ResponseEntity.class)
            .useDefaultResponseMessages(false)
            .enableUrlTemplating(false);
        //.additionalModels(new TypeResolver().resolve(ErrorMessage.class))

        if(type.equals((DOCKET_TYPE_DEFAULT))) {
        } else if(type.equals(DOCKET_TYPE_JWT)) {
            docket
                .securitySchemes(newArrayList(apiKey()))
                .securityContexts(newArrayList(securityContext()));
        }
        return docket;
    }

    public static Docket defaultBuilder(DocketInfo info) {
        return builder(DOCKET_TYPE_DEFAULT, info);
    }

    public static Docket JWTBuilder(DocketInfo info) {
        return builder(DOCKET_TYPE_JWT, info);
    }

    private static ApiKey apiKey() {
        return new ApiKey("BearerToken", "Authorization", "header");
    }

    private static SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/api/.*"))
                .build();
    }

    private static List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(new SecurityReference("BearerToken", authorizationScopes));
    }
}
