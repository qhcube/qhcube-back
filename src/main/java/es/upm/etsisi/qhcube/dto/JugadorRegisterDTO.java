package es.upm.etsisi.qhcube.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class JugadorRegisterDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3296638759161568040L;

    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private String passwordConfirm;

}
