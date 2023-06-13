package foroAlura.api.controller;

import foroAlura.api.domain.curso.Curso;
import foroAlura.api.domain.curso.CursoRepository;
import foroAlura.api.domain.topico.*;
import foroAlura.api.domain.usuario.Usuario;
import foroAlura.api.domain.usuario.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @Operation(summary = "Crear un topico en el foro", description = "Crea un topico en el foro")
    @ApiResponse(responseCode = "201", description = "Created" ,
                    content={@Content(mediaType="application/json",
                            schema=@Schema(implementation=DatosRespuestaTopico.class))})

    @ApiResponse(responseCode = "400", description = "Bad Request",content={@Content(mediaType="application/json")})
    @ApiResponse(responseCode = "403", description = "Forbiden",content={@Content(mediaType="application/json")})

    public ResponseEntity registarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
                                         UriComponentsBuilder uriComponentsBuilder,
                                         Pageable paginacion) {

        //buscar usuario por id, que viene en el datosRegistroTopic
        Optional<Usuario> busquedaUsuario = usuarioRepository.findById(datosRegistroTopico.idUsuario());
        Optional <Curso> busquedaCurso = cursoRepository.findById(datosRegistroTopico.idCurso());

                Usuario usuario = busquedaUsuario.get();
                Curso curso = busquedaCurso.get();

                // No guardar si esta duplicado
                if (topicoRepository.findByTituloAndMensaje(datosRegistroTopico.titulo(), datosRegistroTopico.mensaje(),paginacion).isEmpty()){
                    Topico topico = topicoRepository.save(new Topico(datosRegistroTopico,usuario, curso));
                    DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico);

                    URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.toString()).toUri();
                    return  ResponseEntity.created(url).body(datosRespuestaTopico);
                }
                else {
                    throw new IllegalArgumentException();
                }
    }
    @GetMapping
    @Operation(summary = "Lista todos los topicos en el foro", description = "Lista todos los topicos en el foro, ordenados por fecha de creacion")
    @ApiResponse(responseCode = "200", description = "Ok" ,
            content={@Content(mediaType="application/json",
                    schema=@Schema(implementation=DatosListadoTopicos.class))})
    @ApiResponse(responseCode = "404", description = "Not Found",content={@Content(mediaType="application/json")})
    public ResponseEntity<Page<DatosListadoTopicos>> listadoTopicos(@PageableDefault (size = 10, sort = {"fechaCreacion"})
                                                                        Pageable paginacion ){
        return ResponseEntity.ok(topicoRepository.findAll(paginacion).map(DatosListadoTopicos::new)); //Muestra solo los activos
    }
    @GetMapping ("/{id}")
    @Operation(summary = "Muestra el topico elegido", description = "Busca y muestra el topico pasado por Id")
    @ApiResponse(responseCode = "200", description = "Ok" ,
            content={@Content(mediaType="application/json",
                    schema=@Schema(implementation=DatosListadoTopicos.class))})
    @ApiResponse(responseCode = "404", description = "Not Found",content={@Content(mediaType="application/json")})
    @ApiResponse(responseCode = "403", description = "Forbiden",content={@Content(mediaType="application/json")})
    public ResponseEntity<DatosRespuestaTopico> listadoTopicosById(@PathVariable Long id){
        //DatosListadoTopicos datosListadoTopicos = new DatosListadoTopicos(new Topico());
        Topico topico = topicoRepository.getReferenceById(id);

        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico);
        return ResponseEntity.ok(datosRespuestaTopico);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Actualiza/Modifica el topico elegido", description = "Actualiza/Modifica el topico pasado por Id")
    @ApiResponse(responseCode = "201", description = "Created" ,
            content={@Content(mediaType="application/json",
                    schema=@Schema(implementation=DatosListadoTopicos.class))})
//    @SecurityRequirement(name = "bearer-key")
//    @Parameter(in= ParameterIn.HEADER,name = "Autenticated",
//            description = "Solo un usuario autenticado (con el JWT Token) puede modificar topicos" ,required=true)
    @ApiResponse(responseCode = "403", description = "Forbiden",content={@Content(mediaType="application/json")})
    @ApiResponse(responseCode = "404", description = "Not Found",content={@Content(mediaType="application/json")})
    public ResponseEntity actualizarTopico(@PathVariable Long id,
                                           @RequestBody DatosActualizarTopico datosActualizarTopico,
                                           UriComponentsBuilder uriComponentsBuilder) {

        //buscar usuario por id, que viene en el datosRegistroTopic
        Topico topico = topicoRepository.getReferenceById(id);
        topico.actualizarDatos(datosActualizarTopico);
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico);

        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.toString()).toUri();
        return  ResponseEntity.created(url).body(datosRespuestaTopico);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar topico", description = "Elimina el topico pasado por Id")
    @ApiResponse(responseCode = "200", description = "Ok" ,
            content={@Content(mediaType="application/json",
                    schema=@Schema(implementation=DatosListadoTopicos.class))})
    @ApiResponse(responseCode = "403", description = "Forbiden",content={@Content(mediaType="application/json")})
    @ApiResponse(responseCode = "404", description = "Not Found",content={@Content(mediaType="application/json")})
    public ResponseEntity eliminarTopico(@PathVariable Long id){
         topicoRepository.deleteById(id);
         return ResponseEntity.ok().build();
    }
}
