package es.upm.etsisi.qhcube.dto;

import lombok.*;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter @ Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PlayerDTO implements Serializable {

    private long id;
    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private String jwttoken;
    private boolean enabled;
    private String vericode;

    private List<GameDTO> games;

    private String respStatus;
    private String respMessage;

    public PlayerDTO softClean() {
        this.id = 0;
        this.password = "";
        this.jwttoken = "";
        this.enabled = false;
        this.vericode = "";
        return this;
    }

    public String toJSON() {
        return "{" +
                "\"id\": " + id +
                ", \"nombre\": \"" + nombre + "\"" +
                ", \"apellidos\": \"" + apellidos + "\"" +
                ", \"email\": \"" + email + "\"" +
                ", \"password\": \"" + password + "\"" +
                ", \"jwttoken\": \"" + jwttoken + "\"" +
                ", \"enabled\": " + enabled +
                ", \"vericode\": \"" + vericode + "\"" +
                ", \"games\": " + ((games ==null) ? "\"\"" : games.stream().map(GameDTO::toJSON).collect(Collectors.joining(", ", "[", "]")).replaceAll("\"player\": \"\"", "\"player\": " + this.id)) +
                ", \"respStatus\": \"" + respStatus + "\"" +
                ", \"respMessage\": \"" + respMessage + "\"" +
                "}";
    }

    public String toJavaScript() {
        return "{" +
                "id: " + id +
                ", nombre: '" + nombre + "'" +
                ", apellidos: '" + apellidos + "'" +
                ", email: '" + email + "'" +
                ", password: '" + password + "'" +
                ", jwttoken: '" + jwttoken + "'" +
                ", enabled: " + enabled +
                ", vericode: '" + vericode + "'" +
                ", games: " + ((games ==null) ? "''" : games.stream().map(GameDTO::toJavaScript).collect(Collectors.joining(", ", "{", "}")).replaceAll("player: ''", "player: " + this.id)) +
                ", respStatus: '" + respStatus + "'" +
                ", respMessage: '" + respMessage + "'" +
                "}";
    }
}
