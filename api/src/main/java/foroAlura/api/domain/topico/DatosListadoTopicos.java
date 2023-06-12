package foroAlura.api.domain.topico;

import foroAlura.api.domain.curso.Curso;
import foroAlura.api.domain.status.StatusTopico;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DatosListadoTopicos(Long id, String titulo, String mensaje, LocalDateTime fechaCreacion,
                                  StatusTopico statusTopico, String usuarioNombre, String usuarioEmail,
                                  Curso curso) {

    public DatosListadoTopicos(@NotNull Topico topico){
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getUsuario().getNombre(),
                topico.getUsuario().getEmail(),
                topico.getCurso()
        );
    }

}
