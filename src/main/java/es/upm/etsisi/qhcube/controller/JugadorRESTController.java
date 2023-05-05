package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.dto.JugadorDTO;
import es.upm.etsisi.qhcube.mapper.JugadorMapper;
import es.upm.etsisi.qhcube.service.player.PlayerrService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JugadorRESTController {
    protected final PlayerrService jugadorService;
    protected final JugadorMapper jugadorMapper;
    public JugadorRESTController(PlayerrService jugadorService, JugadorMapper jugadorMapper){
        this.jugadorService = jugadorService;
        this.jugadorMapper = jugadorMapper;
    }


    @GetMapping(path="/get-all-jugadores")
    public ResponseEntity<List<JugadorDTO>> getAllJugadores2(){
        return ResponseEntity.ok(this.jugadorMapper.toDTOs(this.jugadorService.findAll()));
    }
}
