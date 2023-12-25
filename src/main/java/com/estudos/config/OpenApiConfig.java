package com.estudos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    // O Bean é 1 OBJETO que é instanciado, montado e gerenciado pelo Spring IOC Container. Ele busca as infos em XML
    // e anotations ou codig Java sobre como os Beans deve ser configurados / montado. A relação dos Beans é com injeção de dependencias.
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Estudos Spring")
                        .version("v1")
                        .description("Descrição da API")
                        .termsOfService("Link da licensa")
                        .license(new License().name("Apache 2.0")
                                .url("https://url.com")));
    }

}
