package com.vortex.EntregaRapida.service;

import com.vortex.EntregaRapida.dto.request.CidadePorIdRequestDto;
import com.vortex.EntregaRapida.dto.request.CidadeRequestDto;
import com.vortex.EntregaRapida.dto.response.CidadeResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoDeEntidadeException;
import com.vortex.EntregaRapida.exception.custom.ConflitoEntidadeInexistente;
import com.vortex.EntregaRapida.mapper.CidadeMapper;
import com.vortex.EntregaRapida.model.Cidade;
import com.vortex.EntregaRapida.model.Estado;
import com.vortex.EntregaRapida.repository.CidadeRepository;
import com.vortex.EntregaRapida.service.validation.CidadeEstadoValidation;
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
    private CidadeRepository cidadeRepository;
    @Mock
    private CidadeMapper cidadeMapper;
    @Mock
    private CidadeEstadoValidation cidadeEstadoValidation;
    @InjectMocks
    private CidadeService cidadeService;

    private Estado estado;
    private Cidade cidade;
    private final UUID idPadrao = UUID.randomUUID();
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
        when(cidadeEstadoValidation.validaEstadoPorId(idPadrao)).thenReturn(estado);
        when(cidadeEstadoValidation.validarRetornaCidadeDoEstado(estado)).thenReturn(new ArrayList<>());
        when(cidadeRepository.save(any(Cidade.class))).thenReturn(cidade);
        when(cidadeMapper.toResponse(any())).thenAnswer(invocation -> {
            Cidade c = invocation.getArgument(0);
            return new CidadeResponseDto(c.getId(), c.getNome());
        });

        CidadeResponseDto responseDto = cidadeService.cadastrarCidade(requestDto);

        assertNotNull(responseDto, "Retorno não deveria ser nulo");
        assertEquals(responseDto.id(), cidade.getId(), "Ids não coincidem");
        assertEquals(responseDto.nome(), cidade.getNome(), "Nomes não coincidem");
        verify(cidadeEstadoValidation, times(1)).validaEstadoPorId(idPadrao);
        verify(cidadeEstadoValidation, times(1)).validarRetornaCidadeDoEstado(estado);
        verify(cidadeRepository, times(1)).save(any(Cidade.class));
        verify(cidadeMapper, times(1)).toResponse(any(Cidade.class));
    }

    @Test
    void cadastrarCidade_deveRetornarConflitoEntidadeInexistente_quandoEstadoInexistente() {
        when(cidadeEstadoValidation.validaEstadoPorId(any(UUID.class))).thenReturn(null);

        ConflitoEntidadeInexistente exception = assertThrows(ConflitoEntidadeInexistente.class,
                () -> cidadeService.cadastrarCidade(requestDto));

        assertEquals("estado não encontrado com o id passado.", exception.getMessage(), "Mensagens de erro não coincidem");
        verify(cidadeEstadoValidation, times(1)).validaEstadoPorId(any(UUID.class));
        verifyNoMoreInteractions(cidadeEstadoValidation);
        verifyNoInteractions(cidadeRepository);
    }

    @Test
    void cadastrarCidade_deveRetornarConflitoDeEntidadeException_quandoEstadoJaPossuiCidadeComMesmoNome() {
        when(cidadeEstadoValidation.validaEstadoPorId(idPadrao)).thenReturn(estado);
        when(cidadeEstadoValidation.validarRetornaCidadeDoEstado(estado)).thenReturn(List.of(cidade));

        ConflitoDeEntidadeException exception = assertThrows(ConflitoDeEntidadeException.class,
                () -> cidadeService.cadastrarCidade(requestDto));

        assertEquals("Este estado já possui uma cidade com esse nome.", exception.getMessage());
        verify(cidadeEstadoValidation, times(1)).validaEstadoPorId(idPadrao);
        verify(cidadeEstadoValidation, times(1)).validarRetornaCidadeDoEstado(estado);
        verify(cidadeMapper, never()).toResponse(any());
        verify(cidadeRepository, never()).save(any());
        verifyNoMoreInteractions(cidadeEstadoValidation);
    }

    @Test
    void atualizarCidade_deveRetornarCidadeResponseDto_quandoSucesso() {
        CidadeRequestDto novoRequest = new CidadeRequestDto("Melgaço", idPadrao);

        when(cidadeEstadoValidation.validaEstadoPorId(idPadrao)).thenReturn(estado);
        when(cidadeEstadoValidation.validarRetornaCidadeDoEstado(estado)).thenReturn(new ArrayList<>());
        when(cidadeEstadoValidation.validaCidadePorId(idPadrao)).thenReturn(cidade);
        when(cidadeRepository.save(any(Cidade.class))).thenReturn(cidade);
        when(cidadeMapper.toResponse(any(Cidade.class))).thenAnswer(invocation -> {
            Cidade cidade = invocation.getArgument(0);
            return new CidadeResponseDto(cidade.getId(), cidade.getNome());
        });

        CidadeResponseDto responseDto = cidadeService.atualizarCidade(idPadrao, novoRequest);

        assertNotNull(responseDto, "Restorno não deveria ser nulo.");
        assertEquals(responseDto.id(), cidade.getId(), "Ids não coincidem");
        assertEquals(responseDto.nome(), cidade.getNome(), "Nomes não coincidem");
        assertEquals(cidade.getEstado(), estado, "Estados não coincidem");
        verify(cidadeEstadoValidation, times(1)).validaCidadePorId(any(UUID.class));
        verify(cidadeEstadoValidation, times(1)).validarRetornaCidadeDoEstado(estado);
        verify(cidadeEstadoValidation, times(1)).validaEstadoPorId(any(UUID.class));
        verify(cidadeRepository, times(1)).save(any(Cidade.class));
        verify(cidadeMapper, times(1)).toResponse(any(Cidade.class));
    }

    @Test
    void atualizarCidade_deveRetornarConflitoEntidadeInexistente_quandoEstadoInexistente() {
        when(cidadeEstadoValidation.validaEstadoPorId(any(UUID.class))).thenReturn(null);

        ConflitoEntidadeInexistente exception = assertThrows(ConflitoEntidadeInexistente.class,
                () -> cidadeService.atualizarCidade(idPadrao, requestDto));

        assertEquals("estado não encontrado com o id passado.", exception.getMessage(), "Mensagens de erro não coincidem");
        verify(cidadeEstadoValidation, times(1)).validaEstadoPorId(any(UUID.class));
        verifyNoMoreInteractions(cidadeEstadoValidation);
        verifyNoInteractions(cidadeRepository);
    }

    @Test
    void atualizarCidade_deveRetornarConflitoEntidadeInexistente_quandoCidadeInexistente() {
        when(cidadeEstadoValidation.validaEstadoPorId(any(UUID.class))).thenReturn(estado);
        when(cidadeEstadoValidation.validaCidadePorId(any(UUID.class))).thenReturn(null);

        ConflitoEntidadeInexistente exception = assertThrows(ConflitoEntidadeInexistente.class,
                () -> cidadeService.atualizarCidade(idPadrao, requestDto));

        assertEquals("cidade não encontrado com o id passado.", exception.getMessage());
        verify(cidadeEstadoValidation, times(1)).validaEstadoPorId(any(UUID.class));
        verify(cidadeEstadoValidation, times(1)).validaCidadePorId(any(UUID.class));
        verify(cidadeEstadoValidation, never()).validarRetornaCidadeDoEstado(any(Estado.class));
        verify(cidadeRepository, never()).save(any(Cidade.class));
        verify(cidadeMapper, never()).toResponse(any(Cidade.class));

    }

    @Test
    void atualizarCidade_deveRetornarConflitoDeEntidadeException_quandoEstadoJaPossuiCidadeComMesmoNome() {
        Cidade outraCidade = new Cidade(UUID.randomUUID(), "Breves");
        estado.setCidades(List.of(outraCidade));

        when(cidadeEstadoValidation.validaEstadoPorId(any(UUID.class))).thenReturn(estado);
        when(cidadeEstadoValidation.validaCidadePorId(any(UUID.class))).thenReturn(cidade);
        when(cidadeEstadoValidation.validarRetornaCidadeDoEstado(estado)).thenReturn(List.of(outraCidade));

        ConflitoDeEntidadeException exception = assertThrows(ConflitoDeEntidadeException.class,
                () -> cidadeService.atualizarCidade(idPadrao, requestDto));

        assertEquals("Este estado já possui uma cidade com o nome passado.", exception.getMessage());
        verify(cidadeEstadoValidation, times(1)).validaEstadoPorId(any(UUID.class));
        verify(cidadeEstadoValidation, times(1)).validaCidadePorId(any(UUID.class));
        verify(cidadeEstadoValidation, times(1)).validarRetornaCidadeDoEstado(any(Estado.class));
        verify(cidadeRepository, never()).save(any(Cidade.class));
        verify(cidadeMapper, never()).toResponse(any(Cidade.class));
    }

    @Test
    void deletarCidade_deveRetornaVoid_quandoSucesso() {
        CidadePorIdRequestDto dto =
                new CidadePorIdRequestDto(estado.getId(), cidade.getId());

        when(cidadeEstadoValidation.validaEstadoPorId(idPadrao))
                .thenReturn(estado);
        when(cidadeEstadoValidation.validaCidadePorId(idPadrao))
                .thenReturn(cidade);
        when(cidadeEstadoValidation.validarRetornaCidadeDoEstado(estado))
                .thenReturn(List.of(cidade));

        cidadeService.deletarCidade(dto);
        verify(cidadeRepository, times(1)).delete(cidade);
    }

    @Test
    void deletarCidade_deveRetornaConflitoEntidadeInexistente_quandoEstadoeNaoEncontrada() {
        CidadePorIdRequestDto dto =
                new CidadePorIdRequestDto(estado.getId(), cidade.getId());

        when(cidadeEstadoValidation.validaEstadoPorId(idPadrao))
                .thenReturn(null);

        ConflitoEntidadeInexistente exception =
                assertThrows(ConflitoEntidadeInexistente.class,
                        () -> cidadeService.deletarCidade(dto));
        assertEquals("estado não encontrado com o id passado.", exception.getMessage());
    }

    @Test
    void deletarCidade_deveRetornaConflitoEntidadeInexistente_quandoCidadeNaoEncontrada() {
        CidadePorIdRequestDto dto =
                new CidadePorIdRequestDto(estado.getId(), cidade.getId());

        when(cidadeEstadoValidation.validaEstadoPorId(idPadrao))
                .thenReturn(estado);
        when(cidadeEstadoValidation.validaCidadePorId(idPadrao))
                .thenReturn(cidade);
        when(cidadeEstadoValidation.validarRetornaCidadeDoEstado(estado))
                .thenReturn(new ArrayList<>());

        ConflitoEntidadeInexistente exception =
                assertThrows(ConflitoEntidadeInexistente.class,
                        () -> cidadeService.deletarCidade(dto));
        assertEquals("O estado não possui a cidade pesquisada.", exception.getMessage());
    }
}