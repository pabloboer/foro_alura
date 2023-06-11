package foroAlura.api.domain.topico;

import foroAlura.api.domain.curso.Curso;


public record DatosRespuestaTopico(Long id, String titulo, String mensaje, String usuarioNombre, Curso curso) {

    public DatosRespuestaTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getUsuario().getNombre(),
                topico.getCurso()
                );
    }

}
