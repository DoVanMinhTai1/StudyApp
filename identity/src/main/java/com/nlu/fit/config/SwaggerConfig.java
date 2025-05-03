package com.nlu.fit.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(title = "Customer Service API", description = "Customer API documentation", version = "1.0"),
        security = @SecurityRequirement(name = "firebaseAuth"),
        servers = {@Server(url = "http://localhost:8080", description = "Local Server")}
)
@SecurityScheme(
        name = "firebaseAuth",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {
}
