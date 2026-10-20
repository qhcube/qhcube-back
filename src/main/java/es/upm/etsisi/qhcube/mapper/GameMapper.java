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
    @Mapping(target = "player", qualifiedByName = "oneLevel")
    GameDTO toDTO(Game game);

    @Named("sinJugador")
    @Mapping(target = "player", ignore = true)
    GameDTO toDTOWithoutPlayer(Game game);

    @Named("oneLevel")
    @Mapping(target = "player", ignore = true)
    GameDTO toDTOOneLevel(Game game);

    Game toEntity(GameDTO gameDTO);

    @Named("sinPartidas")
    @IterableMapping(qualifiedByName = "sinPartidas")
    List<GameDTO> toDTOs(List<Game> entities);

    @Named("sinPartidas")
    @IterableMapping(qualifiedByName = "sinJugador")
    List<GameDTO> toDTOsWithoutPlayers(List<Game> entities);

    @Named("oneLevel")
    @IterableMapping(qualifiedByName = "oneLevel")
    List<GameDTO> toDTOsOneLevel(List<Game> value);
}
