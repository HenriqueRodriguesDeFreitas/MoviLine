package com.vortex.EntregaRapida.service;

import com.vortex.EntregaRapida.dto.request.RuaRequestDto;
import com.vortex.EntregaRapida.dto.response.RuaResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoDeEntidadeException;
import com.vortex.EntregaRapida.exception.custom.ConflitoEntidadeInexistente;
import com.vortex.EntregaRapida.mapper.RuaMapper;
import com.vortex.EntregaRapida.model.Rua;
import com.vortex.EntregaRapida.repository.RuaRepository;
import com.vortex.EntregaRapida.service.validation.BairroCidadeValidator;
import com.vortex.EntregaRapida.service.validation.CidadeEstadoValidator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RuaService {
    private final CidadeEstadoValidator cidadeEstadoValidator;
    private final BairroCidadeValidator bairroCidadeValidator;
    private final RuaRepository ruaRepository;
    private final RuaMapper ruaMapper;

    public RuaService(CidadeEstadoValidator cidadeEstadoValidator,
                      BairroCidadeValidator bairroCidadeValidator,
                      RuaRepository ruaRepository,
                      RuaMapper ruaMapper) {
        this.cidadeEstadoValidator = cidadeEstadoValidator;
        this.bairroCidadeValidator = bairroCidadeValidator;
        this.ruaRepository = ruaRepository;
        this.ruaMapper = ruaMapper;
    }

    @Transactional
    public RuaResponseDto cadastrarRua(RuaRequestDto request) {
        verificaCidadePertenceAoEstado(request);
        verificaCidadePossuiBairro(request);

        boolean validacaoCidadePossuiBairro
                = bairroJaPossuiRuaComNome(request.nome(), request.bairroId());

        if (validacaoCidadePossuiBairro) {
            throw new ConflitoDeEntidadeException("Bairro já possui rua com o mesmo nome.");
        }

        var bairro = bairroCidadeValidator.getBairroPorId(request.bairroId());

        Rua novaRua = new Rua(request.nome(), request.cep(), bairro);
        return ruaMapper.toResponse(ruaRepository.save(novaRua));
    }

    @Transactional
    public RuaResponseDto atualizarRua(UUID ruaId, RuaRequestDto requestDto) {
        verificaCidadePertenceAoEstado(requestDto);
        verificaCidadePossuiBairro(requestDto);

        var ruaEncontrada = retornaRuaComIdPassado(ruaId);

        if (!ruaRepository.existsByIdAndBairroId(ruaEncontrada.getId(), requestDto.bairroId())) {
            throw new ConflitoEntidadeInexistente(
                    "Rua não pertence ao bairro informado."
            );
        }

        var bairroEncontrado = bairroCidadeValidator.getBairroPorId(requestDto.bairroId());

        boolean nomeAlterado = !ruaEncontrada.getNome()
                .equalsIgnoreCase(requestDto.nome());

        if (nomeAlterado && bairroJaPossuiRuaComNome(requestDto.nome(), requestDto.bairroId())) {
            throw new ConflitoDeEntidadeException("Bairro já possui uma rua com o mesmo nome.");
        }
        ruaEncontrada.setNome(requestDto.nome());
        ruaEncontrada.setCep(requestDto.cep());
        ruaEncontrada.setBairro(bairroEncontrado);

        return ruaMapper.toResponse(ruaRepository.save(ruaEncontrada));
    }

    private void verificaCidadePertenceAoEstado(RuaRequestDto request) {
        if (!cidadeEstadoValidator.validaCidadePertenceAoEstado(request.cidadeId(), request.estadoId())) {
            throw new ConflitoDeEntidadeException("Estado não possui a cidade informada.");
        }
    }

    private void verificaCidadePossuiBairro(RuaRequestDto request) {
        if (!bairroCidadeValidator.validaCidadePossuiBairro(request.cidadeId(), request.bairroId())) {
            throw new ConflitoEntidadeInexistente("Cidade não possui o bairro informado.");
        }
    }

    private boolean bairroJaPossuiRuaComNome(String nomeRua, UUID bairroId) {
        return ruaRepository.existsByNomeIgnoreCaseAndBairroId(nomeRua, bairroId);
    }

    private Rua retornaRuaComIdPassado(UUID ruaId) {
        return ruaRepository.findById(ruaId)
                .orElseThrow(() -> new ConflitoEntidadeInexistente("Não existe rua com Id passado."));
    }
}
