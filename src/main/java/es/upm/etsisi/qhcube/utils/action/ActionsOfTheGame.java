package es.upm.etsisi.qhcube.utils.action;

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
public class ActionsOfTheGame {

    private List<Action> actions = new ArrayList<>();

    public ActionsOfTheGame(String moves) {
        Arrays.stream(moves
                .replaceAll("],", "];")
                .split(";"))
                .forEach(move -> this.actions.add(new Action(move)));
    }

    public boolean isGameOpen() {
        return this.actions.get(this.actions.size() - 1).getKindOfAction() != 5;
    }
}
