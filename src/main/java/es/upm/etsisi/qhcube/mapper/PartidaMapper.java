package es.upm.etsisi.qhcube.mapper;

import es.upm.etsisi.qhcube.dto.PartidaDTO;
import es.upm.etsisi.qhcube.entity.Partida;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.List;

@Mapper(componentModel = "spring", uses = {
        JugadorMapper.class
})
public interface PartidaMapper {

    @Named("sinPartidas")
    @Mapping(target = "jugador", qualifiedByName = "oneLevel")
    PartidaDTO toDTO(Partida partida);

    @Named("oneLevel")
    @Mapping(target = "jugador", ignore = true)
    PartidaDTO toDTOOneLevel(Partida aula);

    @Named("sinPartidas")
    @IterableMapping(qualifiedByName = "sinPartidas")
    Partida toEntity(PartidaDTO partidaDTO);

    @Named("sinPartidas")
    @IterableMapping(qualifiedByName = "sinPartidas")
    List<PartidaDTO> toDTOs(List<Partida> entities);

    @Named("oneLevel")
    @IterableMapping(qualifiedByName = "oneLevel")
    List<PartidaDTO> toDTOsOneLevel(List<Partida> value);
}
