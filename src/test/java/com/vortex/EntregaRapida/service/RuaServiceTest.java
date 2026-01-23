package com.vortex.EntregaRapida.service;

import com.vortex.EntregaRapida.dto.request.RuaRequestDto;
import com.vortex.EntregaRapida.dto.response.RuaResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoDeEntidadeException;
import com.vortex.EntregaRapida.mapper.RuaMapper;
import com.vortex.EntregaRapida.model.Bairro;
import com.vortex.EntregaRapida.model.Cidade;
import com.vortex.EntregaRapida.model.Estado;
import com.vortex.EntregaRapida.model.Rua;
import com.vortex.EntregaRapida.repository.RuaRepository;
import com.vortex.EntregaRapida.service.validation.BairroCidadeValidator;
import com.vortex.EntregaRapida.service.validation.CidadeEstadoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RuaServiceTest {

    @Mock
    private CidadeEstadoValidator cidadeEstadoValidator;
    @Mock
    private BairroCidadeValidator bairroCidadeValidator;
    @Mock
    private RuaRepository ruaRepository;
    @Mock
    private RuaMapper ruaMapper;

    @InjectMocks
    private RuaService ruaService;

    private RuaRequestDto requestDto;
    private RuaResponseDto ruaResponseDto;

    private Estado estado;
    private Cidade cidade;
    private Bairro bairro;
    private Rua novaRua;
    private final UUID estadoId = UUID.randomUUID();
    private final UUID cidadeId = UUID.randomUUID();
    private final UUID bairroId = UUID.randomUUID();
    private final UUID ruaId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        requestDto =
                new RuaRequestDto(estadoId, cidadeId, bairroId, "Tancredo Neves", "88132622");
        estado = new Estado(requestDto.estadoId(), "Pará");
        cidade = new Cidade(requestDto.cidadeId(), "Breves", estado);
        bairro = new Bairro(requestDto.bairroId(), "Centro", cidade);
        novaRua = new Rua(ruaId, requestDto.nome(), requestDto.cep(), bairro);
    }

    @Test
    void cadastrarRua_deveRetornarRuaResponseDto_casoSucesso() {
        when(cidadeEstadoValidator.validaCidadePertenceAoEstado
                (requestDto.cidadeId(), requestDto.estadoId())).thenReturn(true);
        when(bairroCidadeValidator.validaCidadePossuiBairro
                (requestDto.cidadeId(), requestDto.bairroId())).thenReturn(true);
        when(ruaRepository.existsByNomeIgnoreCaseAndBairroId
                (requestDto.nome(), bairroId)).thenReturn(false);
        when(bairroCidadeValidator.getBairroPorId(bairroId)).thenReturn(bairro);
        when(ruaRepository.save(any(Rua.class))).thenReturn(novaRua);
        when(ruaMapper.toResponse(any(Rua.class))).thenAnswer(invocation -> {
            Rua rua = invocation.getArgument(0);
            String nomeEstado = rua.getBairro().getCidade().getEstado().getNome();
            String nomeCidade = rua.getBairro().getCidade().getNome();
            String nomeBairro = rua.getBairro().getNome();
            return new RuaResponseDto(nomeEstado, nomeCidade, nomeBairro, rua.getId(), rua.getNome(), rua.getCep());
        });

        ruaResponseDto = ruaService.cadastrarRua(requestDto);

        assertNotNull(ruaResponseDto, "Retorno não deveria ser nulo.");
        assertEquals(estado.getNome(), ruaResponseDto.estado(), "Nome dos estados não concidem.");
        assertEquals(cidade.getNome(), ruaResponseDto.cidade(), "Nome das cidades não concidem.");
        assertEquals(bairro.getNome(), ruaResponseDto.bairro(), "Nome dos bairros não concidem.");
        assertEquals(novaRua.getId(), ruaResponseDto.id(), "Ids das ruas não concidem.");
        assertEquals(novaRua.getNome(), ruaResponseDto.nome(), "Nomes das ruas não concidem.");

        verify(ruaRepository, times(1)).save(any(Rua.class));
    }

    @Test
    void cadastrarRua_deveRetornarConflitoDeEntidadeException_quandoCidadeJaPossuiBairroComMesmoNome() {
        when(cidadeEstadoValidator.validaCidadePertenceAoEstado
                (requestDto.cidadeId(), requestDto.estadoId())).thenReturn(true);
        when(bairroCidadeValidator.validaCidadePossuiBairro
                (requestDto.cidadeId(), requestDto.bairroId())).thenReturn(true);
        when(ruaRepository.existsByNomeIgnoreCaseAndBairroId
                (requestDto.nome(), bairroId)).thenReturn(true);

        ConflitoDeEntidadeException exception = assertThrows(ConflitoDeEntidadeException.class,
                () -> ruaService.cadastrarRua(requestDto));

        assertEquals("Bairro já possui rua com o mesmo nome.", exception.getMessage());
        verify(bairroCidadeValidator, never()).getBairroPorId(any(UUID.class));
        verify(ruaRepository, never()).save(any(Rua.class));
        verify(ruaMapper, never()).toResponse(any(Rua.class));
    }

    @Test
    void atualizarRua_deveRetornarRuaResponseDto_casoSucesso() {
        // nome diferente do original
        requestDto = new RuaRequestDto(
                estadoId,
                cidadeId,
                bairroId,
                "Rua Nova",
                "12345-000"
        );
        when(cidadeEstadoValidator.validaCidadePertenceAoEstado(cidadeId, estadoId))
                .thenReturn(true);
        when(bairroCidadeValidator.validaCidadePossuiBairro(cidadeId, bairroId))
                .thenReturn(true);
        when(ruaRepository.findById(ruaId)).thenReturn(Optional.of(novaRua));
        when(ruaRepository.existsByIdAndBairroId(ruaId, bairroId)).thenReturn(true);
        when(bairroCidadeValidator.getBairroPorId(bairroId)).thenReturn(bairro);
        when(ruaRepository.existsByNomeIgnoreCaseAndBairroId(any(String.class), any(UUID.class)))
                .thenReturn(false);
        when(ruaRepository.save(any(Rua.class))).thenReturn(novaRua);
        when(ruaMapper.toResponse(any(Rua.class))).thenAnswer(invocation -> {
           Rua rua = invocation.getArgument(0);
           return new RuaResponseDto(estado.getNome(), cidade.getNome(),
                   bairro.getNome(), rua.getId(), rua.getNome(), rua.getCep());
        });

        RuaResponseDto response = ruaService.atualizarRua(ruaId, requestDto);
        assertNotNull(response, "Retorno não deveria ser nulo.");
    }
}