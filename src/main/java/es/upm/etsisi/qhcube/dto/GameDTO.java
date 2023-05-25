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
    private Timestamp totalGameTime;
    private String movsList;
    private String input;

    private PlayerDTO player;

    public Timestamp getTotalGameTime() {
        return new Timestamp(endTime.getTime() - initialTime.getTime());
    }
}
