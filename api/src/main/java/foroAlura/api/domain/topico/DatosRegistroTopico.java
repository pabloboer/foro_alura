package foroAlura.api.domain.topico;

import foroAlura.api.domain.curso.DatosMapeoCurso;
import foroAlura.api.domain.respuesta.DatosMapeoRespuesta;
import foroAlura.api.domain.usuario.DatosMapeoUsuario;


public record DatosRegistroTopico(String titulo, String mensaje, DatosMapeoUsuario datosMapeoUsuario,
                                  DatosMapeoCurso datosMapeoCurso) {

    public DatosRegistroTopico(Topico topico) {
        this(topico.getTitulo(),
                topico.getMensaje(),
                new DatosMapeoUsuario(topico.getUsuario().getId()),
                new DatosMapeoCurso(topico.getCurso().getId())
                );
    }
}
