package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.dto.PartidaDTO;
import es.upm.etsisi.qhcube.mapper.PartidaMapper;
import es.upm.etsisi.qhcube.service.partida.PartidaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PartidaController {

    private final PartidaService partidaService;
    private final PartidaMapper partidaMapper;

    public PartidaController(PartidaService partidaService, PartidaMapper partidaMapper){
        this.partidaService = partidaService;
        this.partidaMapper = partidaMapper;
    }

    @GetMapping(path="/get-all-partidas")
    public ResponseEntity<List<PartidaDTO>> getAllPartidas(){
        return ResponseEntity.ok(this.partidaMapper.toDTOs(this.partidaService.findAll()));
    }

    @GetMapping(path="/get-partida-by-id/{id}")
    public ResponseEntity<PartidaDTO> getPartidaById(@PathVariable("id") int id){
        return ResponseEntity.ok(this.partidaMapper.toDTO(this.partidaService.findById(id)));
    }
}
