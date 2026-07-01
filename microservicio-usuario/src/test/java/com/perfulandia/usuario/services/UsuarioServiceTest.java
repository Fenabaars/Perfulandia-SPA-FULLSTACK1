package com.perfulandia.usuario.services;

import com.perfulandia.usuario.models.dtos.LoginDTO;
import com.perfulandia.usuario.models.dtos.UsuarioDTO;
import com.perfulandia.usuario.models.entities.Usuario;
import com.perfulandia.usuario.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    private Usuario usuarioMock;
    private UsuarioDTO dtoMock;

    @BeforeEach
    void setUp() {
        usuarioMock = new Usuario("Pedro", "pedro@test.com", "1234", "Calle 1", "CLIENTE");
        usuarioMock.setId(1L);

        dtoMock = new UsuarioDTO();
        dtoMock.setNombre("Pedro");
        dtoMock.setEmail("pedro@test.com");
        dtoMock.setPassword("1234");
    }

    @Test
    void givenUsuarioValido_whenRegistrarUsuario_thenRetornaUsuarioCreado() {
        // Given
        when(repository.existsByEmail("pedro@test.com")).thenReturn(false);
        when(repository.save(any(Usuario.class))).thenReturn(usuarioMock);

        // When
        Usuario resultado = service.registrarUsuario(dtoMock);

        // Then
        assertNotNull(resultado);
        assertEquals("CLIENTE", resultado.getRol());
        verify(repository, times(1)).save(any(Usuario.class));
    }

    @Test
    void givenCredencialesValidas_whenAutenticar_thenRetornaUsuario() {
        // Given
        LoginDTO loginDto = new LoginDTO();
        loginDto.setEmail("pedro@test.com");
        loginDto.setPassword("1234");
        
        when(repository.findByEmail("pedro@test.com")).thenReturn(Optional.of(usuarioMock));

        // When
        Usuario resultado = service.autenticar(loginDto);

        // Then
        assertNotNull(resultado);
        assertEquals("pedro@test.com", resultado.getEmail());
    }

    @Test
    void givenPasswordInvalido_whenAutenticar_thenLanzaExcepcion() {
        // Given
        LoginDTO loginDto = new LoginDTO();
        loginDto.setEmail("pedro@test.com");
        loginDto.setPassword("wrong");
        
        when(repository.findByEmail("pedro@test.com")).thenReturn(Optional.of(usuarioMock));

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.autenticar(loginDto);
        });

        assertEquals("Credenciales inválidas", exception.getMessage());
    }
}
