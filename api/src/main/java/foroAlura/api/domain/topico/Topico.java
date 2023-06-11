package foroAlura.api.domain.topico;

import foroAlura.api.domain.curso.Curso;
import foroAlura.api.domain.respuesta.Respuesta;
import foroAlura.api.domain.status.StatusTopico;
import foroAlura.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
@Validated

public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String titulo;
    @NotNull
    private String mensaje;
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Enumerated
    private StatusTopico status = StatusTopico.NO_RESPONDIDO;

    //@JoinColumn(name = "idUsuario")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Usuario usuario;

    @ManyToOne(fetch =FetchType.LAZY)
    @NotNull
    private Curso curso;

    @OneToMany (mappedBy="topico", cascade=CascadeType.ALL, targetEntity = Respuesta.class)
    private List<Respuesta> respuestas = new ArrayList<>();

    public Topico(DatosRegistroTopico datosRegistroTopico, Usuario usuario, Curso curso) {
        this.titulo = datosRegistroTopico.titulo();
        this.mensaje = datosRegistroTopico.mensaje();
        this.usuario = usuario;
        this.curso = curso;
        this.respuestas = null;
    }

}
