package es.upm.etsisi.qhcube.service.jugador;

import es.upm.etsisi.qhcube.dto.JugadorRegisterDTO;
import es.upm.etsisi.qhcube.dto.RegistroResponseDTO;
import es.upm.etsisi.qhcube.entity.Jugador;
import es.upm.etsisi.qhcube.service.AbstractService;

public interface JugadorService extends AbstractService<Jugador> {
    public RegistroResponseDTO register(JugadorRegisterDTO jugador);
}
