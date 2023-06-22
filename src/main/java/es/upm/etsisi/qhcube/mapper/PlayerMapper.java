package es.upm.etsisi.qhcube.mapper;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.entity.Player;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        GameMapper.class
})
public interface PlayerMapper {

    @Named("sinJugadores")
    @Mapping(target = "respStatus", ignore = true)
    @Mapping(target = "respMessage", ignore = true)
    @Mapping(target = "games", qualifiedByName = "oneLevel")
    PlayerDTO toDTO(Player jugador);

    Player toEntity(PlayerDTO jugadorDTO);

    @Named("sinJugadores")
    @IterableMapping(qualifiedByName = "sinJugadores")
    List<PlayerDTO> toDTOs(List<Player> entities);

    @Named("oneLevel")
    @Mapping(target = "respStatus", ignore = true)
    @Mapping(target = "respMessage", ignore = true)
    @Mapping(target = "games", ignore = true)
    PlayerDTO toDTOOneLevel(Player jugador);
}
