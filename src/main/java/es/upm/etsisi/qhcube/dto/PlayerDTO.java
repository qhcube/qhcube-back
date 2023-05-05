package es.upm.etsisi.qhcube.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class PlayerDTO implements Serializable {

    private int id;
    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private String jwttoken;

    private List<GameDTO> games;
}
