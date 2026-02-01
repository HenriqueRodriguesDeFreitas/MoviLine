package com.vortex.EntregaRapida.service;

import com.vortex.EntregaRapida.dto.request.LocalizacaoParamsRequestDto;
import com.vortex.EntregaRapida.dto.request.RuaRequestDto;
import com.vortex.EntregaRapida.dto.request.RuasDeUmBairroRequestDto;
import com.vortex.EntregaRapida.dto.response.RuaResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoDeEntidadeException;
import com.vortex.EntregaRapida.exception.custom.ConflitoEntidadeInexistente;
import com.vortex.EntregaRapida.mapper.RuaMapper;
import com.vortex.EntregaRapida.model.Rua;
import com.vortex.EntregaRapida.repository.RuaRepository;
import com.vortex.EntregaRapida.service.validation.BairroCidadeValidator;
import com.vortex.EntregaRapida.service.validation.CidadeEstadoValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        verificaCidadePertenceAoEstado(request.cidadeId(), request.estadoId());
        verificaCidadePossuiBairro(request.cidadeId(), request.bairroId());

        boolean validacaoBairroPossuiRua
                = bairroJaPossuiRuaComNome(request.nome(), request.bairroId());

        if (validacaoBairroPossuiRua) {
            throw new ConflitoDeEntidadeException("Bairro já possui rua com o mesmo nome.");
        }

        var bairro = bairroCidadeValidator.getBairroPorId(request.bairroId());

        Rua novaRua = new Rua(request.nome(), request.cep(), bairro);
        return ruaMapper.toResponse(ruaRepository.save(novaRua));
    }

    @Transactional
    public RuaResponseDto atualizarRua(UUID ruaId, RuaRequestDto requestDto) {
        verificaCidadePertenceAoEstado(requestDto.cidadeId(), requestDto.estadoId());
        verificaCidadePossuiBairro(requestDto.cidadeId(), requestDto.bairroId());

        var ruaEncontrada = retornaRuaComIdPassado(ruaId);

        if (!ruaRepository.existsByIdAndBairroId(ruaEncontrada.getId(), requestDto.bairroId())) {
            throw new ConflitoEntidadeInexistente(
                    "Bairro não possui rua com id informado."
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

    public Page<RuaResponseDto> buscarRuasDeUmBairro(RuasDeUmBairroRequestDto requestDto,
                                                     Pageable pageable) {
        verificaCidadePertenceAoEstado(requestDto.cidadeId(), requestDto.estadoId());
        verificaCidadePossuiBairro(requestDto.cidadeId(), requestDto.bairroId());
        return ruaRepository.findByBairroId(requestDto.bairroId(), pageable)
                .map(ruaMapper::toResponse);
    }

    public Page<RuaResponseDto> buscarRuasPorNome(UUID estadoId, UUID cidadeId, UUID bairroId,
                                                  String nome, Pageable pageable) {
        verificaCidadePertenceAoEstado(cidadeId, estadoId);
        verificaCidadePossuiBairro(cidadeId, bairroId);
        return ruaRepository.findByNomeIgnoreCaseContainingAndBairroId(
                        nome, bairroId, pageable)
                .map(ruaMapper::toResponse);
    }

    public RuaResponseDto buscarRuaPorId(LocalizacaoParamsRequestDto params) {
        verificaCidadePertenceAoEstado(params.cidadeId(), params.estadoId());
        verificaCidadePossuiBairro(params.cidadeId(), params.bairroId());

        var rua = retornaRuaComIdPassado(params.ruaId());

        return ruaMapper.toResponse(rua);
    }

    public void deletarRua(LocalizacaoParamsRequestDto params) {
        verificaCidadePertenceAoEstado(params.cidadeId(), params.estadoId());
        verificaCidadePossuiBairro(params.cidadeId(), params.bairroId());

        if(!ruaRepository.existsByIdAndBairroId(params.ruaId(), params.bairroId())){
            throw new ConflitoEntidadeInexistente("Bairro não possui rua com o Id informado, não será possível deletar.");
        }

        var rua = retornaRuaComIdPassado(params.ruaId());
        ruaRepository.delete(rua);
    }

    private void verificaCidadePertenceAoEstado(UUID cidadeId, UUID estadoId) {
        if (!cidadeEstadoValidator.validaCidadePertenceAoEstado(cidadeId, estadoId)) {
            throw new ConflitoDeEntidadeException("Estado não possui a cidade informada.");
        }
    }

    private void verificaCidadePossuiBairro(UUID cidadeId, UUID bairroId) {
        if (!bairroCidadeValidator.validaCidadePossuiBairro(cidadeId, bairroId)) {
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
