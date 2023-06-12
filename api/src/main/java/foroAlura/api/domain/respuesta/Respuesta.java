package foroAlura.api.domain.respuesta;

import foroAlura.api.domain.topico.Topico;
import foroAlura.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "respuestas")
@Entity(name = "Respuesta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensaje;

    @ManyToOne //(fetch = FetchType.LAZY)
    private Topico topico;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToOne //(fetch = FetchType.LAZY)
    private Usuario usuario;

    private boolean solucion;
}
