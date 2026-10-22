package es.upm.etsisi.qhcube.utils.input;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputTest {

    @Test
    void isNearSolvedInput() {
        Input nearSolvedInput = new Input();
        nearSolvedInput.setNearSolvedInput();
        assertFalse(nearSolvedInput.isSolved());
        nearSolvedInput.permute(1, 0, 1);
        assertTrue(nearSolvedInput.isSolved());
    }
}
