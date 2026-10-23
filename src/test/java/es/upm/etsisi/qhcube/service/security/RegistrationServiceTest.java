package es.upm.etsisi.qhcube.service.security;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.dto.RegisterRequestDTO;
import es.upm.etsisi.qhcube.entity.Player;
import es.upm.etsisi.qhcube.mapper.PlayerMapper;
import es.upm.etsisi.qhcube.repository.PlayerRepository;
import es.upm.etsisi.qhcube.utils.EmailUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailSendException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {
    private PlayerRepository repository;
    private PlayerMapper mapper;
    private EmailUtil emailUtil;
    private RegistrationService service;

    @BeforeEach
    void setUp() {
        this.repository = mock(PlayerRepository.class);
        this.mapper = mock(PlayerMapper.class);
        this.emailUtil = mock(EmailUtil.class);
        this.service = new RegistrationService(repository, mapper, emailUtil);
    }

    @Test
    void registerWithoutFieldsReturnsValidationError() {
        PlayerDTO result = this.service.register(new RegisterRequestDTO());

        assertEquals("ERROR", result.getRespStatus());
        assertEquals("El nombre no puede estar vacío.", result.getRespMessage());
    }

    @Test
    void registerDeletesPlayerWhenEmailFails() throws Exception {
        Player savedPlayer = new Player();
        when(this.repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(this.mapper.toEntity(any())).thenReturn(savedPlayer);
        when(this.repository.save(any())).thenReturn(savedPlayer);
        doThrow(new MailSendException("Servidor de correo caído")).when(this.emailUtil).sendVerificationEmail(any());

        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setNombre("Ana");
        request.setApellidos("Gomez");
        request.setEmail("ana@upm.es");
        request.setPassword("secreta");

        PlayerDTO result = this.service.register(request);

        assertEquals("ERROR", result.getRespStatus());
        verify(this.repository).delete(savedPlayer);
    }

    @Test
    void validateEmailOfEnabledPlayerReturnsAlreadyVerified() {
        Player player = new Player()
            .setEmail("ana@upm.es")
            .setEnabled(true)
            .setVericode(null);
        when(this.repository.findByEmail("ana@upm.es")).thenReturn(Optional.of(player));

        PlayerDTO result = assertDoesNotThrow(() -> this.service.validateEmail("codigo-antiguo", "ana@upm.es"));

        assertEquals("OK", result.getRespStatus());
        verify(this.repository, never()).save(any());
    }

    @Test
    void validateEmailWithWrongCodeReturnsError() {
        Player player = new Player()
            .setEmail("ana@upm.es")
            .setEnabled(false)
            .setVericode("abc");
        when(this.repository.findByEmail("ana@upm.es")).thenReturn(Optional.of(player));

        PlayerDTO result = this.service.validateEmail("este vericode no es abc", "ana@upm.es");
        assertFalse(player.isEnabled());
        assertEquals("ERROR", result.getRespStatus());
        verify(this.repository, never()).save(any());
    }

    @Test
    void validateEmailWithCorrectCodeEnablesPlayer() {
        Player player = new Player()
            .setEmail("ana@upm.es")
            .setEnabled(false)
            .setVericode("abc");
        when(this.repository.findByEmail("ana@upm.es")).thenReturn(Optional.of(player));

        PlayerDTO result = this.service.validateEmail("abc", "ana@upm.es");
        assertTrue(player.isEnabled());
        assertNull(player.getVericode());
        assertEquals("OK", result.getRespStatus());
        verify(this.repository).save(player);
    }

    @Test
    void resendToEnabledPlayerDoesNotSendEmail() throws Exception {
        Player player = new Player()
            .setEmail("ana@upm.es")
            .setEnabled(true)
            .setVericode(null);
        when(this.repository.findByEmail("ana@upm.es")).thenReturn(Optional.of(player));

        PlayerDTO result = this.service.resendVerificationEmail(new PlayerDTO().setEmail("ana@upm.es"));
        assertEquals("ERROR", result.getRespStatus());
        verify(this.emailUtil, never()).sendVerificationEmail(any());
    }
}
