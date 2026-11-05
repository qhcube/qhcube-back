package es.upm.etsisi.qhcube.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomStringUtilTest {

    @Test
    public void generatesAlphanumericStringOfRequestedLength() {
        String code = RandomStringUtil.getAlphaNumericString(64);

        assertEquals(64, code.length());
        assertTrue(code.matches("[a-zA-Z0-9]+"));
    }
}
