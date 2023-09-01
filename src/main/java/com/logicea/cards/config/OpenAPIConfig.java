package com.logicea.cards.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {

        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("shadrak.kai@gmail.com");
        contact.setName("Shadrack Kai");
        contact.setUrl("https://github.com/shadrack-kai/cards-api");

        Info info = new Info()
                .title("Manage Cards API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage task cards").termsOfService("https://github.com/shadrack-kai/cards-api");

        return new OpenAPI().info(info).servers(List.of(devServer));
    }

}
