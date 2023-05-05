package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.mapper.PlayerMapper;
import es.upm.etsisi.qhcube.service.player.PlayerrService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerRESTController {
    protected final PlayerrService jugadorService;
    protected final PlayerMapper jugadorMapper;
    public PlayerRESTController(PlayerrService jugadorService, PlayerMapper jugadorMapper){
        this.jugadorService = jugadorService;
        this.jugadorMapper = jugadorMapper;
    }


    @GetMapping(path="/get-all-jugadores")
    public ResponseEntity<List<PlayerDTO>> getAllJugadores2(){
        return ResponseEntity.ok(this.jugadorMapper.toDTOs(this.jugadorService.findAll()));
    }
}
