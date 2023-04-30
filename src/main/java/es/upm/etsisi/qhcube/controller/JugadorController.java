package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.dto.JugadorDTO;
import es.upm.etsisi.qhcube.entity.Jugador;
import es.upm.etsisi.qhcube.mapper.JugadorMapper;
import es.upm.etsisi.qhcube.service.jugador.JugadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@RestController
public class JugadorController {
    private final JugadorService jugadorService;
    private final JugadorMapper jugadorMapper;
    public JugadorController(JugadorService jugadorService, JugadorMapper jugadorMapper){
        this.jugadorService = jugadorService;
        this.jugadorMapper = jugadorMapper;
    }

    // TODO: Sustituir por ResponseEntity y quitar el return
    @PostMapping(path="/add")
    public @ResponseBody String addNewJugador (@RequestParam String nombre
            , @RequestParam String email, @RequestParam String apellidos) {

        Jugador jugador = new Jugador();
        jugador.setNombre(nombre);
        jugador.setEmail(email);
        jugador.setApellidos(apellidos);
        return "Saved";
    }

    @GetMapping(path="/get-all-jugadores")
    public ResponseEntity<List<JugadorDTO>> getAllJugadores2(){
        return ResponseEntity.ok(this.jugadorMapper.toDTOs(this.jugadorService.findAll()));
    }
}
