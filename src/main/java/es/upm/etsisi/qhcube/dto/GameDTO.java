package es.upm.etsisi.qhcube.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.sql.Timestamp;

@Getter
@Setter
@Accessors(chain = true)
public class GameDTO {

    private int id;
    private Timestamp initialTime;
    private Timestamp endTime;
    private Timestamp tiempoJuego;

    private PlayerDTO player;

    public Timestamp getTiempoJuego() {
        return new Timestamp(endTime.getTime() - initialTime.getTime());
    }
}
