package com.mile.nokenzivot.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
class OpenApiConfig {

  @Bean
  GroupedOpenApi api() {
    return GroupedOpenApi.builder()
        .group("mile.gjore")
        .pathsToMatch("/**")
        .build();
  }

  @Bean
  OpenAPI apiInfo() {
    return new OpenAPI().info(new Info().title("Night Life Service - REST API")
        .description("This service provides integration between the services NMM and SYMBIOT")
        .version("1.0.0")
        .termsOfService("Iskratel")
        .license(new License().name("Iskratel Standard Licence")
            .url("https://www.iskratel.mk/en/")));
  }

}
