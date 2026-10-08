package es.upm.etsisi.qhcube.utils.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
public class InputManager {

    public static void setRandomInput(Input input) {
        for (int i = 0; i < 4; i++) {
            input.getRowA()[i].setRandomCoordinate();
            input.getRowB()[i].setRandomCoordinate();
        }
    }

    public static void randomPermute(Input input) {
        int elem = (int) (Math.random() * 2);
        int j = (int) (Math.random() * 4);
        int k = (int) (Math.random() * 4);

        if (j != k) {
            input.permute(elem, j, k);
        }
    }

    public static void randomExchange(Input input) {
        input.exchange((int) (Math.random() * 2), (int) (Math.random() * 4));
    }

    public static void randomRotate(Input input) {
        input.rotate((int) (Math.random() * 2), (int) (Math.random() * 4), (int) (Math.random() * 4));
    }
}
