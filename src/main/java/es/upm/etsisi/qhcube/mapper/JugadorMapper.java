package es.upm.etsisi.qhcube.mapper;

import es.upm.etsisi.qhcube.dto.JugadorDTO;
import es.upm.etsisi.qhcube.entity.Jugador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring", uses = {
        PartidaMapper.class
})
public interface JugadorMapper {

    @Mapping(target="partidas" , source = "partidasById")
    JugadorDTO toDTO(Jugador jugador);

    @Mapping(target="partidasById" , source = "partidas")
    Jugador toEntity(JugadorDTO jugadorDTO);

    List<JugadorDTO> toDTOs(List<Jugador> entities);
}
