package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.utils.input.Input;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.service.player.PlayerService;
import es.upm.etsisi.qhcube.utils.input.InputManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Objects;

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

        Input initialInput = new Input();
        initialInput.setInitialPracticeInput();

        for (int i = 0; i < 9; i++) {
            InputManager.randomPermute(initialInput);
            InputManager.randomExchange(initialInput);
        }

        GameDTO newGameDTO = GameDTO.builder()
                .initialTime(new java.sql.Timestamp(System.currentTimeMillis()))
                .input(initialInput.toString())
                .player(playerDTO)
                .build();

        PlayerDTO playerInDB = this.playerService.findByEmail(playerDTO.getEmail());

        if (playerInDB == null || !Objects.equals(playerInDB.getJwttoken(), playerDTO.getJwttoken())) {
            newGameDTO.softClean().getPlayer().setRespStatus("ERROR").setRespMessage("El usuario no existe.");
        }

        newGameDTO.getPlayer().softClean();

        return newGameDTO;
    }
}
