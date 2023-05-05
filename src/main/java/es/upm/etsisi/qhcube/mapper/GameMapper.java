package es.upm.etsisi.qhcube.mapper;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.entity.Game;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.List;

@Mapper(componentModel = "spring", uses = {
        PlayerMapper.class
})
public interface GameMapper {

    @Named("sinPartidas")
    @Mapping(target = "jugador", qualifiedByName = "oneLevel")
    GameDTO toDTO(Game partida);

    @Named("oneLevel")
    @Mapping(target = "jugador", ignore = true)
    GameDTO toDTOOneLevel(Game aula);

    @Named("sinPartidas")
    @IterableMapping(qualifiedByName = "sinPartidas")
    Game toEntity(GameDTO partidaDTO);

    @Named("sinPartidas")
    @IterableMapping(qualifiedByName = "sinPartidas")
    List<GameDTO> toDTOs(List<Game> entities);

    @Named("oneLevel")
    @IterableMapping(qualifiedByName = "oneLevel")
    List<GameDTO> toDTOsOneLevel(List<Game> value);
}
