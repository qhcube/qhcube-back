package es.upm.etsisi.qhcube.controller.free;

import es.upm.etsisi.qhcube.controller.JugadorRESTController;
import es.upm.etsisi.qhcube.mapper.JugadorMapper;
import es.upm.etsisi.qhcube.service.jugador.JugadorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${apiFreePrefix}/jugador")
public class JugadorFreeRESTController  extends JugadorRESTController {

    public JugadorFreeRESTController(JugadorService jugadorService, JugadorMapper jugadorMapper) {
        super(jugadorService, jugadorMapper);
    }
}
