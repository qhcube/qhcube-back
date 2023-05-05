package es.upm.etsisi.qhcube.controller.api;

import es.upm.etsisi.qhcube.controller.JugadorRESTController;
import es.upm.etsisi.qhcube.dto.JugadorDTO;
import es.upm.etsisi.qhcube.entity.Player;
import es.upm.etsisi.qhcube.mapper.JugadorMapper;
import es.upm.etsisi.qhcube.service.player.PlayerrService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/jugador")
public class JugadorPVTRESTController extends JugadorRESTController {

    public JugadorPVTRESTController(PlayerrService jugadorService, JugadorMapper jugadorMapper) {
        super(jugadorService, jugadorMapper);
    }

    // TODO: Sustituir por ResponseEntity y quitar el return
    @PostMapping(path="/add")
    public @ResponseBody String addNewJugador (@RequestParam String nombre
            , @RequestParam String email, @RequestParam String apellidos) {

        Player jugador = new Player();
        jugador.setNombre(nombre);
        jugador.setEmail(email);
        jugador.setApellidos(apellidos);
        return "Saved";
    }

    @GetMapping(path="/get-all")
    public ResponseEntity<List<JugadorDTO>> getAll(){
        return ResponseEntity.ok(this.jugadorMapper.toDTOs(this.jugadorService.findAll()));
    }
}
