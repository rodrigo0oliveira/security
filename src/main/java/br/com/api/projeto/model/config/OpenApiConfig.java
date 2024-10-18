package br.com.api.projeto.model.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Security API")
                        .description("API segura de reservas")
                        .version("1.0")
                        .license(new License()
                                .url("https://github.com/rodrigo0oliveira")));
    }
}
