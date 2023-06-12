package foroAlura.api.domain.topico;

import foroAlura.api.domain.curso.Curso;
import foroAlura.api.domain.status.StatusTopico;
import foroAlura.api.domain.usuario.Usuario;
import jakarta.validation.constraints.NotNull;


public record DatosActualizarTopico(@NotNull String titulo, @NotNull String mensaje,
                                    StatusTopico statusTopico, Usuario usuario, Curso curso) {

    public DatosActualizarTopico(Topico topico) {
        this(topico.getTitulo(),
                topico.getMensaje(),
                topico.getStatus(),
                topico.getUsuario(),
                topico.getCurso()
        );
    }
}
