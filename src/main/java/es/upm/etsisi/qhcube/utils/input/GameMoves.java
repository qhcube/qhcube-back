package es.upm.etsisi.qhcube.utils.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Getter @Setter
@NoArgsConstructor
public class GameMoves {

    private List<Move> moves = new ArrayList<>();

    public GameMoves(String moves) {
        Arrays.stream(moves
                .replaceAll("],", "];")
                .split(";"))
                .forEach(move -> {
                    this.moves.add(new Move(move));
        });
    }

    public boolean isGameOpen() {
        return this.moves.get(this.moves.size() - 1).getKindOfAction() != 5;
    }
}
