package foroAlura.api.controller;

import foroAlura.api.domain.curso.Curso;
import foroAlura.api.domain.curso.CursoRepository;
import foroAlura.api.domain.topico.DatosRegistroTopico;
import foroAlura.api.domain.topico.DatosRespuestaTopico;
import foroAlura.api.domain.topico.Topico;
import foroAlura.api.domain.topico.TopicoRepository;
import foroAlura.api.domain.usuario.Usuario;
import foroAlura.api.domain.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public String hola(){
        return "Hola";
    }
    @PostMapping
    public ResponseEntity registarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
                                         UriComponentsBuilder uriComponentsBuilder,
                                         Pageable paginacion) {

        //buscar usuario por id, que viene en el datosRegistroTopic
        Optional<Usuario> busquedaUsuario = usuarioRepository.findById(datosRegistroTopico.datosMapeoUsuario().id());
        Optional <Curso> busquedaCurso = cursoRepository.findById(datosRegistroTopico.datosMapeoCurso().id());
        if (busquedaUsuario.isEmpty()) {
            return ResponseEntity.badRequest().body("No existe el usuario");
        }
        else if (busquedaCurso.isEmpty()){
            return ResponseEntity.badRequest().body("Curso no encontrado");
        }
        else{
            Usuario usuario = busquedaUsuario.get();
            Curso curso = busquedaCurso.get();

            // No guardar si esta duplicado
            if (topicoRepository.findByTituloAndMensaje(datosRegistroTopico.titulo(), datosRegistroTopico.mensaje(),paginacion).isEmpty()){
                System.out.println("unico");
                Topico topico = topicoRepository.save(new Topico(datosRegistroTopico,usuario, curso));
                DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico);

                URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.toString()).toUri();
                return  ResponseEntity.created(url).body(datosRespuestaTopico);
            }
            else {
                System.out.println("duplicado");
                return ResponseEntity.badRequest().body("Topico duplicado");
            }
        }
    }
}
