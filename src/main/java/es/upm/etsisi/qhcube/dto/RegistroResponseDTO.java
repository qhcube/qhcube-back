package es.upm.etsisi.qhcube.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.io.Serial;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class RegistroResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8602182172951770525L;

    private final String nombre;
    private final String apellidos;
    private final String email;
    private final String jwttoken;

}
