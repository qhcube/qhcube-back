package es.upm.etsisi.qhcube.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DTOSerializationTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private PlayerDTO player() {
        return new PlayerDTO()
            .setEmail("jugador@upm.es")
            .setPassword("hash-de-la-password")
            .setJwttoken("token-del-jugador")
            .setVericode("codigo-de-verificacion");
    }

    @Test
    void playerNeverExposesPasswordNorVericode() throws Exception {
        String json = this.mapper.writeValueAsString(this.player());
        assertFalse(json.contains("hash-de-la-password"));
        assertFalse(json.contains("codigo-de-verificacion"));
        assertTrue(json.contains("token-del-jugador"), "El login necesita devolver el token.");
    }

    @Test
    void playerPasswordIsStillAcceptedInRequests() throws Exception {
        PlayerDTO player = this.mapper.readValue(
            "{\"email\":\"jugador@upm.es\",\"password\":\"secreta\",\"vericode\":\"intento\"}", PlayerDTO.class);
        assertEquals("secreta", player.getPassword());
        assertNotEquals("intento", player.getVericode());
    }

    @Test
    void gameNeverExposesPlayerToken() throws Exception {
        GameDTO game = GameDTO.builder().build().setPlayer(this.player());
        String json = this.mapper.writeValueAsString(game);
        assertFalse(json.contains("token-del-jugador"));
        assertFalse(json.contains("hash-de-la-password"));
        assertFalse(json.contains("codigo-de-verificacion"));
    }
}
