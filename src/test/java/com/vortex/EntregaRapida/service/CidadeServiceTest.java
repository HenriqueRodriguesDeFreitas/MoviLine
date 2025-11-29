package com.vortex.EntregaRapida.service;

import com.vortex.EntregaRapida.dto.request.CidadeRequestDto;
import com.vortex.EntregaRapida.dto.response.CidadeResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoDeEntidadeException;
import com.vortex.EntregaRapida.exception.custom.ConflitoEntidadeInexistente;
import com.vortex.EntregaRapida.mapper.CidadeMapper;
import com.vortex.EntregaRapida.model.Cidade;
import com.vortex.EntregaRapida.model.Estado;
import com.vortex.EntregaRapida.repository.CidadeRepository;
import com.vortex.EntregaRapida.repository.EstadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CidadeServiceTest {

    @Mock
    private EstadoRepository estadoRepository;
    @Mock
    private CidadeRepository cidadeRepository;
    @Mock
    private CidadeMapper cidadeMapper;
    @InjectMocks
    private CidadeService cidadeService;

    private Estado estado;
    private Cidade cidade;
    private UUID idPadrao = UUID.randomUUID();
    private CidadeRequestDto requestDto;

    @BeforeEach
    void setUp() {
        estado = new Estado(idPadrao, "Pará");
        cidade = new Cidade(idPadrao, "Breves");
        cidade.setEstado(estado);

        requestDto = new CidadeRequestDto(cidade.getNome(), cidade.getEstado().getId());
    }

    @Test
    void cadastrarCidade_deveRetornarCidadeResponseDto_quandoSucesso() {
        when(estadoRepository.buscarEstadoSimplesPorId(idPadrao)).thenReturn(Optional.of(estado));
        when(cidadeRepository.buscarCidadesPorEstado(estado.getId())).thenReturn(new ArrayList<>());
        when(cidadeRepository.save(any(Cidade.class))).thenReturn(cidade);
        when(cidadeMapper.toResponse(any())).thenAnswer(invocation -> {
            Cidade c = invocation.getArgument(0);
            return new CidadeResponseDto(c.getId(), c.getNome());
        });

        CidadeResponseDto responseDto = cidadeService.cadastrarCidade(requestDto);

        assertNotNull(responseDto, "Retorno não deveria ser nulo");
        assertEquals(responseDto.id(), cidade.getId(), "Ids não coincidem");
        assertEquals(responseDto.nome(), cidade.getNome(), "Nomes não coincidem");
        verify(estadoRepository, times(1)).buscarEstadoSimplesPorId(idPadrao);
        verify(cidadeRepository, times(1)).buscarCidadesPorEstado(estado.getId());
        verify(cidadeRepository, times(1)).save(any(Cidade.class));
        verify(cidadeMapper, times(1)).toResponse(any(Cidade.class));
    }

    @Test
    void cadastrarCidade_deveRetornarConflitoEntidadeInexistente_quandoEstadoInexistente() {
        when(estadoRepository.buscarEstadoSimplesPorId(any(UUID.class))).thenReturn(Optional.empty());

        ConflitoEntidadeInexistente exception = assertThrows(ConflitoEntidadeInexistente.class,
                () -> cidadeService.cadastrarCidade(requestDto));

        assertEquals("Nenhum estado encontrado com o id passado.", exception.getMessage(), "Mensagens de erro não coincidem");
        verify(estadoRepository, times(1)).buscarEstadoSimplesPorId(any(UUID.class));
        verifyNoMoreInteractions(estadoRepository);
        verifyNoInteractions(cidadeRepository);
    }

    @Test
    void cadastrarCidade_deveRetornarConflitoDeEntidadeException_quandoEstadoJaPossuiCidadeComMesmoNome() {
        when(estadoRepository.buscarEstadoSimplesPorId(idPadrao)).thenReturn(Optional.of(estado));
        when(cidadeRepository.buscarCidadesPorEstado(estado.getId())).thenReturn(List.of(cidade));

        ConflitoDeEntidadeException exception = assertThrows(ConflitoDeEntidadeException.class,
                () -> cidadeService.cadastrarCidade(requestDto));

        assertEquals("Este estado já possui uma cidade com esse nome.", exception.getMessage());
        verify(estadoRepository, times(1)).buscarEstadoSimplesPorId(idPadrao);
        verify(cidadeRepository, times(1)).buscarCidadesPorEstado(estado.getId());
        verify(cidadeMapper, never()).toResponse(any());
        verify(cidadeRepository, never()).save(any());
        verifyNoMoreInteractions(estadoRepository);
        verifyNoMoreInteractions(cidadeRepository);
    }
}