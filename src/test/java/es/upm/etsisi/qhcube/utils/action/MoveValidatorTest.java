package es.upm.etsisi.qhcube.utils.action;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveValidatorTest {

    @Test
    void nullMove() {
        assertFalse(MoveValidator.isValidMove(null));
    }

    @Test
    void tooLongText() {
        assertFalse(MoveValidator.isValidMove("[4," + "1".repeat(150) + ",0,0]"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"[0,1,0,3]", "[0,1,2,3]", "[1,2,0,-1]", "[2,1,-1,-1]", "[3,1,3,-1]",
        "[0,2,3,0]", "[4,0.748,0.632,0.2]", "[4,1e-7,0.5,0.5]"})
    void validMoves(String move) {
        assertTrue(MoveValidator.isValidMove(move));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "hola", "[0,1,2]", "[0,1,2,3,4]", "0,1,2,3", "[0,3,1,1]", "[0,1,7,1]",
        "[0,1.5,1,1]", "[1,1,0,2]", "[2,1,0,-1]", "[5,1,1,1]", "[9,1,1,1]", "[4,NaN,0,0]", "[4,Infinity,0,0]",
        "[0,1,-1,2]", "[0,1,4,0]"})
    void notValidMoves(String move) {
        assertFalse(MoveValidator.isValidMove(move));
    }

}
