package es.upm.etsisi.qhcube.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.io.Serial;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class NewRegisterResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8602182172951770525L;

    private final String status;
    private final String nombre;
    private final String apellidos;
    private final String email;
    private final String jwttoken;

    //TODO: Borrar toString()


    @Override
    public String toString() {
        return "NewRegisterResponseDTO{" +
                "status='" + status + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", jwttoken='" + jwttoken + '\'' +
                '}';
    }
}
