package es.upm.etsisi.qhcube.controller.free;

import es.upm.etsisi.qhcube.controller.PartidaRESTController;
import es.upm.etsisi.qhcube.mapper.PartidaMapper;
import es.upm.etsisi.qhcube.service.partida.PartidaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${apiFreePrefix}/partida")
public class PartidaFreeRESTController extends PartidaRESTController {

    public PartidaFreeRESTController(PartidaService partidaService, PartidaMapper partidaMapper) {
        super(partidaService, partidaMapper);
    }
}
