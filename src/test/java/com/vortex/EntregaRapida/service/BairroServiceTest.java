package com.vortex.EntregaRapida.service;

import com.vortex.EntregaRapida.dto.request.BairroRequestDto;
import com.vortex.EntregaRapida.dto.response.BairroResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoEntidadeInexistente;
import com.vortex.EntregaRapida.mapper.BairroMapper;
import com.vortex.EntregaRapida.model.Bairro;
import com.vortex.EntregaRapida.model.Cidade;
import com.vortex.EntregaRapida.model.Estado;
import com.vortex.EntregaRapida.repository.BairroRepository;
import com.vortex.EntregaRapida.repository.CidadeRepository;
import com.vortex.EntregaRapida.service.validation.BairroCidadeValidator;
import com.vortex.EntregaRapida.service.validation.CidadeEstadoValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BairroServiceTest {

    @Mock
    BairroRepository bairroRepository;
    @Mock
    CidadeRepository cidadeRepository;
    @Mock
    BairroCidadeValidator bairroCidadeValidator;
    @Mock
    CidadeEstadoValidation cidadeEstadoValidation;
    @Mock
    BairroMapper bairroMapper;
    @InjectMocks
    BairroService bairroService;

    private final UUID idPardrao = UUID.randomUUID();
    Bairro bairro;
    Cidade cidade;
    Estado estado;
    BairroRequestDto requestDto;
    BairroResponseDto responseDto;

    @BeforeEach
    void setUp() {
        estado = new Estado(idPardrao, "Pará");
        cidade = new Cidade(idPardrao, "Breves");
        bairro = new Bairro(idPardrao, "Aeroporto", cidade);
        requestDto = new BairroRequestDto(estado.getId(),
                cidade.getId(),
                bairro.getNome());
    }

    @Test
    void cadastrarBairro_deveRetornarBairroResponseDto_quandoSucesso() {
        when(cidadeEstadoValidation.validaCidadePertenceAoEstado(idPardrao, idPardrao))
                .thenReturn(true);
        when(bairroCidadeValidator.cidadePossuiBairroComNomePassado
                (anyString(), any(UUID.class))).thenReturn(false);
        when(cidadeEstadoValidation.validaCidadePorId(any(UUID.class))).thenReturn(cidade);
        when(bairroRepository.save(any(Bairro.class))).thenReturn(bairro);
        when(bairroMapper.toResponse(any(Bairro.class))).thenAnswer(invocation -> {
            Bairro b = invocation.getArgument(0);
            return new BairroResponseDto(b.getId(), b.getNome());
        });

        BairroResponseDto responseDto = bairroService.cadastrarBairro(requestDto);

        assertNotNull(responseDto, "Retorno não poderia ser nulo.");
        assertEquals(bairro.getId(), responseDto.id(), "Ids não coincidem.");
        assertEquals(bairro.getNome(), responseDto.nome(), "Nomes não coincidem.");

        verify(cidadeEstadoValidation, times(1))
                .validaCidadePertenceAoEstado(eq(cidade.getId()), eq(estado.getId()));
        verify(bairroCidadeValidator, times(1))
                .cidadePossuiBairroComNomePassado(bairro.getNome(), cidade.getId());
        verify(cidadeEstadoValidation, times(1)).validaCidadePorId(cidade.getId());
        verify(bairroRepository, times(1)).save(any(Bairro.class));
        verify(bairroMapper, times(1)).toResponse(any(Bairro.class));

    }

    @Test
    void cadastrarBairo_deveRetornarConflitoEntidadeInexistente_quandoEstadoNaoPossuiCidade(){
        when(cidadeEstadoValidation.validaCidadePertenceAoEstado(any(UUID.class), any(UUID.class)))
                .thenReturn(false);

        ConflitoEntidadeInexistente exception = assertThrows(ConflitoEntidadeInexistente.class,
                () -> bairroService.cadastrarBairro(requestDto));

        assertEquals("O estado não possui a cidade pesquisada.", exception.getMessage(), "Mensagens não coincidem.");
    }

    @Test
    void atualizarBairro() {
    }
}