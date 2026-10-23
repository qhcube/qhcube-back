package es.upm.etsisi.qhcube.service.security;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.dto.RegisterRequestDTO;
import es.upm.etsisi.qhcube.entity.Player;
import es.upm.etsisi.qhcube.mapper.PlayerMapper;
import es.upm.etsisi.qhcube.repository.PlayerRepository;
import es.upm.etsisi.qhcube.utils.EmailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailSendException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {
    private final RegistrationService service = new RegistrationService(
        mock(PlayerRepository.class), mock(PlayerMapper.class), mock(EmailUtil.class));

    @Test
    void registerWithoutFieldsReturnsValidationError() {
        PlayerDTO result = this.service.register(new RegisterRequestDTO());

        assertEquals("ERROR", result.getRespStatus());
        assertEquals("El nombre no puede estar vacío.", result.getRespMessage());
    }

    @Test
    void registerDeletesPlayerWhenEmailFails() throws Exception {
        PlayerRepository repository = mock(PlayerRepository.class);
        PlayerMapper mapper = mock(PlayerMapper.class);
        EmailUtil emailUtil = mock(EmailUtil.class);
        RegistrationService service = new RegistrationService(repository, mapper, emailUtil);

        Player savedPlayer = new Player();
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(mapper.toEntity(any())).thenReturn(savedPlayer);
        when(repository.save(any())).thenReturn(savedPlayer);
        doThrow(new MailSendException("Servidor de correo caído")).when(emailUtil).sendVerificationEmail(any());

        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setNombre("Ana");
        request.setApellidos("Gomez");
        request.setEmail("ana@upm.es");
        request.setPassword("secreta");

        PlayerDTO result = service.register(request);

        assertEquals("ERROR", result.getRespStatus());
        verify(repository).delete(savedPlayer);
    }
}
