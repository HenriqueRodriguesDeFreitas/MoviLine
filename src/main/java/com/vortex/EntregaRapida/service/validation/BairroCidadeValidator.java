package com.vortex.EntregaRapida.service.validation;

import com.vortex.EntregaRapida.repository.BairroRepository;
import com.vortex.EntregaRapida.repository.CidadeRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BairroCidadeValidator {

    private final BairroRepository bairroRepository;
    private final CidadeRepository cidadeRepository;

    public BairroCidadeValidator(BairroRepository bairroRepository, CidadeRepository cidadeRepository) {
        this.bairroRepository = bairroRepository;
        this.cidadeRepository = cidadeRepository;
    }

    public boolean cidadePossuiBairroComNomePassado(String nome, UUID cidadeId){
        return bairroRepository.existsByNomeIgnoreCaseAndCidadeId(nome, cidadeId);
    }
}
