package com.GabrielTiziano.Notify.config;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI getOpenAPI(){
        Contact contact = new Contact();
        contact.setUrl("https://github.com/GGhiaroni");
        contact.name("Gabriel Ghiaroni Tiziano");
        contact.setEmail("gghiaronitiziano@gmail.com");

        Info info = new Info();
        info.title("Notify API");
        info.version("v1.0");
        info.description("Gateway de notificações escalável construído com Java e Spring Boot. Possui segurança JWT stateless, integrações externas desacopladas via OpenFeign, rastreamento de estado com MongoDB e rate-limiting com Redis.");
        info.contact(contact);

        SecurityScheme securityScheme = new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(info)
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme));
    }
}
