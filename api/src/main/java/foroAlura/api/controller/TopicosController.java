package foroAlura.api.controller;

import foroAlura.api.domain.curso.Curso;
import foroAlura.api.domain.curso.CursoRepository;
import foroAlura.api.domain.topico.*;
import foroAlura.api.domain.usuario.Usuario;
import foroAlura.api.domain.usuario.UsuarioRepository;
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
    public ResponseEntity registarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
                                         UriComponentsBuilder uriComponentsBuilder,
                                         Pageable paginacion) {

        //buscar usuario por id, que viene en el datosRegistroTopic
        Optional<Usuario> busquedaUsuario = usuarioRepository.findById(datosRegistroTopico.idUsuario());
        Optional <Curso> busquedaCurso = cursoRepository.findById(datosRegistroTopico.idCurso());
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
                Topico topico = topicoRepository.save(new Topico(datosRegistroTopico,usuario, curso));
                DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico);

                URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.toString()).toUri();
                return  ResponseEntity.created(url).body(datosRespuestaTopico);
            }
            else {
                return ResponseEntity.badRequest().body("Topico duplicado");
            }


        }
    }
    @GetMapping
    public ResponseEntity<Page<DatosListadoTopicos>> listadoTopicos(@PageableDefault (size = 10, sort = {"fechaCreacion"})
                                                                        Pageable paginacion ){

        return ResponseEntity.ok(topicoRepository.findAll(paginacion).map(DatosListadoTopicos::new)); //Muestra solo los activos
    }
    @GetMapping ("/{id}")
    public ResponseEntity<DatosRespuestaTopico> listadoTopicosById(@PathVariable Long id){
        //DatosListadoTopicos datosListadoTopicos = new DatosListadoTopicos(new Topico());
        Topico topico = topicoRepository.getReferenceById(id);

        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico);
        return ResponseEntity.ok(datosRespuestaTopico);
    }


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity actualizarTopico(@PathVariable Long id,
                                           @RequestBody @Valid DatosActualizarTopico datosActualizarTopico,
                                           UriComponentsBuilder uriComponentsBuilder) {

        //buscar usuario por id, que viene en el datosRegistroTopic
        Topico topico = topicoRepository.getReferenceById(id);
        topico.actualizarDatos(datosActualizarTopico);
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico);

        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.toString()).toUri();
        return  ResponseEntity.created(url).body(datosRespuestaTopico);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity eliminarTopico(@PathVariable Long id){
        topicoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
