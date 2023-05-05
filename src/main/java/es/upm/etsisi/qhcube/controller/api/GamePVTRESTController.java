package es.upm.etsisi.qhcube.controller.api;

import es.upm.etsisi.qhcube.controller.GameRESTController;
import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.service.game.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/partida")
public class GamePVTRESTController extends GameRESTController {

    public GamePVTRESTController(GameService partidaService, GameMapper partidaMapper) {
        super(partidaService, partidaMapper);
    }

    @GetMapping(path="/get-all")
    public ResponseEntity<List<GameDTO>> getAll(){
        return ResponseEntity.ok(this.partidaMapper.toDTOs(this.partidaService.findAll()));
    }

    @GetMapping(path="/get-by-id/{id}")
    public ResponseEntity<GameDTO> getById(@PathVariable("id") int id){
        return ResponseEntity.ok(this.partidaMapper.toDTO(this.partidaService.findById(id)));
    }
}
