package es.upm.etsisi.qhcube.service.security;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.dto.RegisterRequestDTO;
import es.upm.etsisi.qhcube.mapper.PlayerMapper;
import es.upm.etsisi.qhcube.repository.PlayerRepository;
import es.upm.etsisi.qhcube.utils.EmailUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class RegistrationServiceTest {
    private final RegistrationService service = new RegistrationService(
        mock(PlayerRepository.class), mock(PlayerMapper.class), mock(EmailUtil.class));

    @Test
    void registerWithoutFieldsReturnsValidationError() {
        PlayerDTO result = this.service.register(new RegisterRequestDTO());

        assertEquals("ERROR", result.getRespStatus());
        assertEquals("El nombre no puede estar vacío.", result.getRespMessage());
    }
}
