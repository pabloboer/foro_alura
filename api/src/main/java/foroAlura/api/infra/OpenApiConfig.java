package foroAlura.api.infra;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "REST API Foro Alura", version = "1.0",
        description = "REST API Foro Alura es una API de manejo de foros. La API es un CRUD que se podrá realizar a cada entidad creada y que funciona así:\n" +
                "* Crear un nuevo registro.\n" +
                "* Mostrar todos registros.\n" +
                "* Mostrar un registro específico.\n" +
                "* Actualizar un registro.\n" +
                "* Eliminar un registro.",
        contact = @Contact(name = "Pablo Boer",
                            email = "boer.pablo@gmail.com")),
        security = {@SecurityRequirement(name = "JWT Token")}
)
@SecuritySchemes({
        @SecurityScheme(name = "JWT Token", type = SecuritySchemeType.HTTP,
                scheme = "bearer", bearerFormat = "JWT")
})


public class OpenApiConfig {

}