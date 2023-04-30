package es.upm.etsisi.qhcube.mapper;

import es.upm.etsisi.qhcube.dto.JugadorDTO;
import es.upm.etsisi.qhcube.entity.Jugador;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        PartidaMapper.class
})
public interface JugadorMapper {

    @Named("sinJugadores")
    @Mapping(target = "partidas", qualifiedByName = "oneLevel")
    JugadorDTO toDTO(Jugador jugador);

    Jugador toEntity(JugadorDTO jugadorDTO);

    @Named("sinJugadores")
    @IterableMapping(qualifiedByName = "sinJugadores")
    List<JugadorDTO> toDTOs(List<Jugador> entities);

    @Named("oneLevel")
    @Mapping(target = "partidas", ignore = true)
    JugadorDTO toDTOOneLevel(Jugador jugador);
}
