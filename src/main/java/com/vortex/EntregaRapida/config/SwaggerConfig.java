package com.vortex.EntregaRapida.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customizarOpenApi(){
        final String nomeEsquemaSeguranca = "bearerAuth";

        return new OpenAPI()
                .info(new Info().title("MoviLine")
                        .description("API REST de logística para transportadoras de pequeno e médio porte.")
                        .version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList(nomeEsquemaSeguranca))
                .components(new Components()
                        .addSecuritySchemes(nomeEsquemaSeguranca,
                                 new SecurityScheme()
                                         .name(nomeEsquemaSeguranca)
                                         .type(SecurityScheme.Type.HTTP)
                                         .scheme("bearer")
                                         .bearerFormat("JWT")));
    }
}
