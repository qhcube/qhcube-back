package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.service.player.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.stream.IntStream;

@Slf4j
@Service
public class InputServiceImpl implements InputService {

    private final PlayerService playerService;

    public InputServiceImpl(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public GameDTO initRegisteredGame(PlayerDTO playerDTO) {
        playerDTO.setRespStatus("OK");
        playerDTO.setRespMessage("Juego iniciado correctamente.");

        GameDTO newGameDTO = GameDTO.builder()
                .initialTime(new java.sql.Timestamp(System.currentTimeMillis()))
                .input(this.generateRandomInput())
                .player(playerDTO)
                .build();
        PlayerDTO playerInDB = this.playerService.findByEmail(playerDTO.getEmail());

        if (playerInDB == null || !Objects.equals(playerInDB.getJwttoken(), playerDTO.getJwttoken())) {
            newGameDTO.softClean().getPlayer().setRespStatus("ERROR").setRespMessage("El usuario no existe.");
        }

        newGameDTO.getPlayer().softClean();

        return newGameDTO;
    }

    private String generateRandomInput() {
        int[] number = IntStream.range(0, 32)
                .mapToObj(i -> String.valueOf((int) (Math.random() * 100)))
                .mapToInt(Integer::parseInt)
                .toArray();

        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < number.length; i++) {
            if (i%16 == 0)
                result.append("[");
            if (i%4 == 0)
                result.append("[");
            result.append(number[i]);
            if (i%4 == 3)
                result.append("]");
            if (i%16 == 15)
                result.append("]");
            if (i<31)
                result.append(",");
        }

        return result + "]";


    }
}
