package es.upm.etsisi.qhcube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.sql.Timestamp;

@Getter
@Setter
@Accessors(chain = true)
public class PartidaDTO {

    private int id;
    private Timestamp tiempoInicial;
    private Timestamp tiempoFinal;
    private Timestamp tiempoJuego;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JugadorDTO jugador;

    public Timestamp getTiempoJuego() {
        return new Timestamp(tiempoFinal.getTime() - tiempoInicial.getTime());
    }
}
