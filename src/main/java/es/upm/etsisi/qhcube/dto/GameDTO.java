package es.upm.etsisi.qhcube.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@Accessors(chain = true)
public class GameDTO {

    private long id;
    @Builder.Default
    private Timestamp initialTime = Timestamp.valueOf("2001-01-01 00:00:00");
    @Builder.Default
    private Timestamp endTime = Timestamp.valueOf("2000-01-01 00:00:00");
    @Builder.Default
    private Timestamp totalGameTime = Timestamp.valueOf("2000-01-01 00:00:00");
    private String movsList;
    private String input;
    private String currentInput;

    private PlayerDTO player;

    public GameDTO softClean() {
        this.id = 0;
        this.initialTime = Timestamp.valueOf("2001-01-01 00:00:00");
        this.endTime = Timestamp.valueOf("2000-01-01 00:00:00");
        this.totalGameTime = Timestamp.valueOf("2000-01-01 00:00:00");
        this.movsList = "";
        this.input = "";
        return this;
    }

   public String toJSON() {
               return "{" +
               "\"id\": " + id +
               ", \"initialTime\": \"" + initialTime + "\"" +
               ", \"endTime\": \"" + endTime + "\"" +
               ", \"totalGameTime\": \"" + totalGameTime + "\"" +
               ", \"movsList\": \"" + movsList + "\"" +
               ", \"input\": \"" + input + "\"" +
               ", \"player\": " + (player == null ? "\"\"" : player.getId()) +
               "}";
   }

   public String toJavaScript() {
               return "{" +
               "id: " + id +
               ", initialTime: '" + initialTime + "'" +
               ", endTime: '" + endTime + "'" +
               ", totalGameTime: '" + totalGameTime + "'" +
               ", movsList: '" + movsList + "'" +
               ", input: '" + input + "'" +
               ", player: " + (player == null ? "''" : player.getId()) +
               "}";
   }

    public Timestamp getTotalGameTime() {
        if (endTime.before(initialTime))
            return new Timestamp(System.currentTimeMillis() - initialTime.getTime());
        else
            return new Timestamp(endTime.getTime() - initialTime.getTime());
    }
}
