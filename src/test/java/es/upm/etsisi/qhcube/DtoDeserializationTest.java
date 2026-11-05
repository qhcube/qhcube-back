package es.upm.etsisi.qhcube;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DtoDeserializationTest {

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void playerWithoutIdIsDeserialized() {
        String json = """
            {"email": "ana@upm.es", "password": "secreto"}
            """;

        PlayerDTO player = this.jsonMapper.readValue(json, PlayerDTO.class);

        assertEquals(0, player.getId());
        assertEquals("ana@upm.es", player.getEmail());
    }

    @Test
    void gameKeepsDefaultValuesForMissingFields() {
        String json = """
            {"movsList": "abc"}
            """;

        GameDTO game = this.jsonMapper.readValue(json, GameDTO.class);

        assertNotNull(game.getInitialTime());
        assertEquals("abc", game.getMovsList());
    }
}
