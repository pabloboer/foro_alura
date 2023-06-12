package foroAlura.api.domain.topico;


import jakarta.validation.constraints.NotNull;

public record DatosRegistroTopico(@NotNull String titulo, @NotNull String mensaje,Long idUsuario,
                                  Long idCurso) {
    public DatosRegistroTopico(Topico topico) {
        this(topico.getTitulo(),
                topico.getMensaje(),
                topico.getUsuario().getId(),
                topico.getCurso().getId()
                );
    }
}
