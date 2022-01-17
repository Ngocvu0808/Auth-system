package com.ttt.mar.notify.swagger;

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author KietDT
 * @created_date 05/05/2021
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

  private static final Logger LOGGER = LogManager.getLogger(Swagger2Config.class);

  private static final String DEFAULT_API_DESCRIPTION = "Description about the APIs";
  private static final String AUTHORIZATION = "Authorization";
  private static final String SOURCE_CONTROLLER = "com.ttt.mar.notify.controller";

  @Bean
  public Docket apiDocket() {

    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(getApiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage(SOURCE_CONTROLLER))
        .paths(PathSelectors.any())
        .build()
        .enable(true)
        .securityContexts(Lists.newArrayList(securityContext()))
        .securitySchemes(Lists.newArrayList(apiKey()));
  }

  private ApiKey apiKey() {
    return new ApiKey(AUTHORIZATION, AUTHORIZATION, "header");
  }

  @Bean
  public SecurityConfiguration security() {
    return new SecurityConfiguration(
        null,
        null,
        null, // realm Needed for authenticate button to work
        null, // appName Needed for authenticate button to work
        "access_token",// apiKeyValue
        ApiKeyVehicle.HEADER,
        AUTHORIZATION, //apiKeyName
        null);
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(defaultAuth())
        .build();
  }

  private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope
        = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Lists.newArrayList(
        new SecurityReference(AUTHORIZATION, authorizationScopes));
  }

  private ApiInfo getApiInfo() {
    return new ApiInfoBuilder()
        .title("Demo Swagger service API Doc")
        .description(DEFAULT_API_DESCRIPTION)
        .build();
  }
}