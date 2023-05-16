package es.upm.etsisi.qhcube.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Getter @ Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PlayerDTO implements Serializable {

    private int id;
    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private String jwttoken;
    private boolean enabled;
    private String verificationCode;

    private List<GameDTO> games;

    private String respStatus;
    private String respMessage;

    @Override
    public String toString() {
        return "PlayerDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", jwttoken='" + jwttoken + '\'' +
                ", games=" + games +
                ", respStatus='" + respStatus + '\'' +
                ", respMessage='" + respMessage + '\'' +
                '}';
    }
}
