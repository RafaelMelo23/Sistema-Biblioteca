package com.poo.projeto_final.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info (
                title = "Aplicação web biblioteca",
                version = "1.0.0",
                description = "Aplicação web implementando Domain Driven Design, APIs REST e Spring HATEOAS"
        ),
        servers = {
                @Server(url = "/", description = "Servidor local")
        }
)
public class OpenApiConfig {
}