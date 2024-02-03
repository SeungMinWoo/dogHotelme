package com.green.hoteldog.common;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(title = "Dog Hotelling Service Ver.1"
                ,description = "애견호텔링중개서비스 v1"
                ,version = "1.0.0")
        ,security = @SecurityRequirement(name = "authorization")
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP
        ,name = "authorization"
        ,in = SecuritySchemeIn.HEADER
        ,bearerFormat = "JWT"
        ,scheme = "Bearer"
)
public class SwaggerConfiguration {

}
