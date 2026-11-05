package es.upm.etsisi.qhcube.utils;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailUtilTest {

    private static final String VERIFY_URL = "http://localhost/email-validation?code=abc&email=a@b.es";

    @Test
    void playerNameIsHtmlEscaped() {
        PlayerDTO player = new PlayerDTO()
            .setNombre("<a href=\"http://malo\">Pulsa</a>")
            .setApellidos("<b>X</b>");

        String content = EmailUtil.buildVerificationContent(player, VERIFY_URL, 3);

        assertFalse(content.contains("<a href=\"http://malo\">"), "El enlace malicioso no debe llegar como HTML.");
        assertFalse(content.contains("<b>X</b>"));
        assertTrue(content.contains("&lt;a href=&quot;http://malo&quot;&gt;Pulsa&lt;/a&gt;"));
    }

    @Test
    void normalNameAndVerifyUrlAreIncluded() {
        PlayerDTO player = new PlayerDTO().setNombre("Ana").setApellidos("Gomez");

        String content = EmailUtil.buildVerificationContent(player, VERIFY_URL, 3);
        assertTrue(content.contains("3 días"));
        assertTrue(content.contains("Estimado <b>Ana Gomez</b>"));
        assertTrue(content.contains(VERIFY_URL));
    }
}
