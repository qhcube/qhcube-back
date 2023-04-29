package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.entity.Jugador;
import es.upm.etsisi.qhcube.repository.JugadorRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class JugadorController {
    private final JugadorRepository jugadorRepository;

    public JugadorController(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewJugador (@RequestParam String nombre
            , @RequestParam String email, @RequestParam String apellidos) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        Jugador jugador = new Jugador();
        jugador.setNombre(nombre);
        jugador.setEmail(email);
        jugador.setApellidos(apellidos);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Jugador> getAllJugadores() {
        // This returns a JSON or XML with the users
        return this.jugadorRepository.findAll();
    }
}
