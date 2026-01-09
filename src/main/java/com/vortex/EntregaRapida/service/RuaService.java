package com.vortex.EntregaRapida.service;

import com.vortex.EntregaRapida.dto.request.RuaRequestDto;
import com.vortex.EntregaRapida.dto.response.RuaResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoDeEntidadeException;
import com.vortex.EntregaRapida.exception.custom.ConflitoEntidadeInexistente;
import com.vortex.EntregaRapida.repository.RuaRepository;
import com.vortex.EntregaRapida.service.validation.BairroCidadeValidator;
import com.vortex.EntregaRapida.service.validation.CidadeEstadoValidator;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.UnknownFormatFlagsException;

@Service
public class RuaService {
    private final CidadeEstadoValidator cidadeEstadoValidator;
    private final BairroCidadeValidator bairroCidadeValidator;
    private final RuaRepository ruaRepository;

    public RuaService(CidadeEstadoValidator cidadeEstadoValidator,
                      BairroCidadeValidator bairroCidadeValidator,
                      RuaRepository ruaRepository) {
        this.cidadeEstadoValidator = cidadeEstadoValidator;
        this.bairroCidadeValidator = bairroCidadeValidator;
        this.ruaRepository = ruaRepository;
    }

    public RuaResponseDto cadastrarRua(RuaRequestDto request) {
        verificaCidadePertenceAoEstado(request);
        verificaCidadePossuiBairro(request);

        boolean validacaoCidadePossuiBairro
                = verificaBairroPossuiRuaComMesmoNome(request.bairroId(), request.nome());

        if(validacaoCidadePossuiBairro) {
            throw new ConflitoDeEntidadeException("Bairro já possui rua com o mesmo nome.");
        }


    }

    private void verificaCidadePertenceAoEstado(RuaRequestDto request) {
        if (!cidadeEstadoValidator.validaCidadePertenceAoEstado(request.cidadeId(), request.estadoId())) {
            throw new UnknownFormatFlagsException("Estado não possui a cidade informada.");
        }
    }

    private void verificaCidadePossuiBairro(RuaRequestDto request) {
        if (!bairroCidadeValidator.validaCidadePossuiBairro(request.cidadeId(), request.bairroId())) {
            throw new ConflitoEntidadeInexistente("Cidade não possui o bairro informado.");
        }
    }

    private boolean verificaBairroPossuiRuaComMesmoNome(UUID bairroId, String nomeRua) {
        if (ruaRepository.existsByNomeIgnoreCaseAndBairroId(nomeRua, bairroId)) {
            return true;
        } else {
            return false;
        }
    }
}
