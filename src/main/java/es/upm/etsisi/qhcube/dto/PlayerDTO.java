package es.upm.etsisi.qhcube.dto;

import lombok.*;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Arrays;
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

    @Override
    public String toString() {
        return "PlayerDTO {" +
                "id: " + id +
                ", nombre: '" + nombre + '\'' +
                ", apellidos: '" + apellidos + '\'' +
                ", email: '" + email + '\'' +
                ", password: '" + password + '\'' +
                ", jwttoken: '" + jwttoken + '\'' +
                ", games: " + games.stream().map(GameDTO::toString).collect(Collectors.joining(", ", "{", "}")) +
                ", respStatus: '" + respStatus + '\'' +
                ", respMessage: '" + respMessage + '\'' +
                '}';
    }
}
