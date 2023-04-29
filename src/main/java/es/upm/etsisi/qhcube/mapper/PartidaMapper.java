package es.upm.etsisi.qhcube.mapper;

import es.upm.etsisi.qhcube.dto.PartidaDTO;
import es.upm.etsisi.qhcube.entity.Partida;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        JugadorMapper.class
})
public interface PartidaMapper {

    PartidaDTO toDTO(Partida partida);

    Partida toEntity(PartidaDTO partidaDTO);
}
