package foroAlura.api.controller;

import foroAlura.api.domain.topico.DatosRespuestaTopico;
import foroAlura.api.domain.usuario.Usuario;
import foroAlura.api.domain.usuario.UsuarioRepository;
import foroAlura.api.infra.security.TokenService;
import foroAlura.api.usuario.DatosAutenticacionUsuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private TokenService tokenService;

        @Autowired
        private UsuarioRepository usuarioRepository;

       @Operation(summary = "Login de usuario", description = "Login de usuario")
       @ApiResponses(
                value = {
                        @ApiResponse (responseCode = "201", description = "Created" ,
                                        content={@Content(mediaType="application/json",
                                        schema=@Schema(implementation=DatosAutenticacionUsuario.class))
                                        }
                        )
                }
       )
       @ApiResponse(responseCode = "404", description = "Not Found",content={@Content(mediaType="application/json")})
       @PostMapping
        public ResponseEntity autenticacionUsuario (@RequestBody @Valid DatosAutenticacionUsuario datosAutenticacionUsuario){

            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(datosAutenticacionUsuario.nombre(),
                    datosAutenticacionUsuario.contrasena());

            var usuarioAutenticado = authenticationManager.authenticate(authenticationToken);

            var JWTtoken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
            return ResponseEntity.ok().body(JWTtoken);

        }
}
