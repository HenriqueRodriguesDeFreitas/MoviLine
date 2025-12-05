package com.vortex.EntregaRapida.service;

import com.vortex.EntregaRapida.dto.request.EstadoRequestDto;
import com.vortex.EntregaRapida.dto.response.EstadoResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoDeEntidadeException;
import com.vortex.EntregaRapida.exception.custom.ConflitoEntidadeInexistente;
import com.vortex.EntregaRapida.mapper.EstadoMapper;
import com.vortex.EntregaRapida.model.Estado;
import com.vortex.EntregaRapida.repository.EstadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoServiceTest {

    @Mock
    private EstadoRepository estadoRepository;
    @Mock
    private EstadoMapper estadoMapper;
    @InjectMocks
    private EstadoService estadoService;

    private Estado estado;
    private EstadoRequestDto estadoRequest;
    private EstadoResponseDto estadoResponse;
    UUID idPadrao = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        estado = new Estado("Pará");
        estado.setId(idPadrao);

        estadoResponse = new EstadoResponseDto(estado.getId(), estado.getNome());

        estadoRequest = new EstadoRequestDto("Pará");
    }

    @Test
    void cadastrarEstado_casoNaoTenhaOutroEstadoComMesmoNome_deveRetornarSucesso() {
        when(estadoRepository.findByNomeIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(estadoRepository.save(any(Estado.class))).thenReturn(estado);
        when(estadoMapper.toResponse(any(Estado.class))).thenReturn(estadoResponse);
        when(estadoMapper.toEntity(any())).thenReturn(estado);

        EstadoResponseDto resposta = estadoService.cadastrarEstado(estadoRequest);

        assertNotNull(resposta);
        assertEquals(estado.getId(), resposta.id(), "Ids não coincidem.");
        assertEquals(estado.getNome(), resposta.nome(), "Nomes não coincidem");

        verify(estadoRepository, times(1)).findByNomeIgnoreCase(anyString());
        verify(estadoRepository, times(1)).save(any(Estado.class));
        verify(estadoMapper).toEntity(any(EstadoRequestDto.class));
        verify(estadoMapper).toResponse(any(Estado.class));
        verifyNoMoreInteractions(estadoRepository);
    }

    @Test
    void cadastrarEstado_casoTenhaOutroEstadoComMesmoNome_deveRetornarErro() {
        when(estadoRepository.findByNomeIgnoreCase("Pará")).thenReturn(Optional.of(estado));

        ConflitoDeEntidadeException exception = assertThrows(ConflitoDeEntidadeException.class,
                () -> estadoService.cadastrarEstado(estadoRequest));

        assertEquals("Já existe estado com o nome passado.", exception.getMessage());
        verify(estadoRepository, times(1)).findByNomeIgnoreCase(estadoRequest.nome());
        verifyNoMoreInteractions(estadoRepository);
    }

    @Test
    void atualizarEstado_casoIdEncontrado_casoNomeNaoCadastrado_deveAtualizarEstado() {
        when(estadoRepository.findById(idPadrao)).thenReturn(Optional.of(estado));
        when(estadoRepository.findByNomeIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(estadoRepository.save(any(Estado.class))).thenReturn(estado);
        when(estadoMapper.toResponse(any())).thenAnswer(invocation -> {
            Estado e = invocation.getArgument(0);
            return new EstadoResponseDto(e.getId(), e.getNome());
        });

        EstadoResponseDto response = estadoService.atualizarEstado(idPadrao, "Paraná");

        System.out.println(estado.getNome());
        assertNotNull(response);
        assertEquals(response.id(), estado.getId(), "Ids não coincidem.");
        assertEquals(response.nome(), estado.getNome(), "Nomes não coincidem.");
    }

    @Test
    void atualizarEstado_casoNaoIdEncontrado_deveRetornarErro() {
        when(estadoRepository.findById(idPadrao)).thenReturn(Optional.empty());

        ConflitoEntidadeInexistente exception = assertThrows(ConflitoEntidadeInexistente.class,
                () -> estadoService.atualizarEstado(idPadrao, "Paraná"));

        assertEquals("Nenhum estado encontrado com o id passado.", exception.getMessage());
        verify(estadoRepository, times(1)).findById(idPadrao);
        verifyNoMoreInteractions(estadoRepository);
    }

    @Test
    void atualizarEstado_casoNomeJaCadastrado_deveRetornarErro() {
        estado.setNome("Pará");
        Estado outroEstado = new Estado(UUID.randomUUID(), "Amazonas");

        when(estadoRepository.findById(idPadrao)).thenReturn(Optional.of(estado));
        when(estadoRepository.findByNomeIgnoreCase("Amazonas")).thenReturn(Optional.of(outroEstado));

        ConflitoDeEntidadeException exception = assertThrows(ConflitoDeEntidadeException.class,
                () -> estadoService.atualizarEstado(idPadrao, "Amazonas"));

        assertEquals("Já existe estado com o nome passado.", exception.getMessage());
        verify(estadoRepository, times(1)).findById(idPadrao);
        verify(estadoRepository, times(1)).findByNomeIgnoreCase(anyString());
        verifyNoMoreInteractions(estadoRepository);
        verifyNoInteractions(estadoMapper);
    }
}