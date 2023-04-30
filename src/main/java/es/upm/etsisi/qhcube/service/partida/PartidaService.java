package es.upm.etsisi.qhcube.service.partida;

import es.upm.etsisi.qhcube.entity.Partida;
import es.upm.etsisi.qhcube.service.AbstractService;

public interface PartidaService extends AbstractService<Partida> {
    Partida findById(int id);
}
