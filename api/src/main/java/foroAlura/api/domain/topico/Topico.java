package foroAlura.api.domain.topico;

import foroAlura.api.domain.curso.Curso;
import foroAlura.api.domain.respuesta.Respuesta;
import foroAlura.api.domain.status.StatusTopico;
import foroAlura.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")

public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Enumerated
    private StatusTopico status = StatusTopico.NO_RESPONDIDO;

    //@JoinColumn(name = "idUsuario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @ManyToOne(fetch =FetchType.LAZY)
    private Curso curso;

    @OneToMany (mappedBy="topico", cascade=CascadeType.ALL, targetEntity = Respuesta.class)
    private List<Respuesta> respuestas = new ArrayList<>();

}
