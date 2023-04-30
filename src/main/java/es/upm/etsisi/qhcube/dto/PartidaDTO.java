package es.upm.etsisi.qhcube.dto;

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

    private JugadorDTO jugador;

    public Timestamp getTiempoJuego() {
        return new Timestamp(tiempoFinal.getTime() - tiempoInicial.getTime());
    }
}
