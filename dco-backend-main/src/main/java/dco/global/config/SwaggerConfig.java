package dco.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@OpenAPIDefinition(
        info = @Info(title = "dco API 명세서",
                description = "학생 계정   " +
                        "\n[여기 토큰 나중에 삽입]    " +
                        "\n[여기 토큰 나중에 삽입]    " +
                        "\n교수 계정    " +
                        "\n[여기 토큰 나중에 삽입]    " +
                        "\n[여기 토큰 나중에 삽입]    ",
                version = "v1"))
@Configuration
public class SwaggerConfig {

    /**
     * http://localhost:8080/swagger-ui/index.html
     */

    // JWT + swagger
    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement));
    }

}