package br.com.fiap.hora_certa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI horaCertaOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Hora Certa API")
                .version("v1")
                .description("API do Hora Certa: cuidadores, idosos, medicamentos e registro de doses. "
                        + "Cada remédio na hora certa. Cada família em paz."));
    }
}
