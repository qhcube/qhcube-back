package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.dto.GameDTO;

public interface MoveService {
    GameDTO addMove(String jwttoken, String move);
}
