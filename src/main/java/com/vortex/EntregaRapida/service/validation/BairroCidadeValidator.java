package com.vortex.EntregaRapida.service.validation;

import com.vortex.EntregaRapida.exception.custom.ConflitoEntidadeInexistente;
import com.vortex.EntregaRapida.model.Bairro;
import com.vortex.EntregaRapida.model.Cidade;
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

    public boolean cidadePossuiBairroComNomePassado(String nome, UUID cidadeId) {
        return bairroRepository.existsByNomeIgnoreCaseAndCidadeId(nome, cidadeId);
    }

    public boolean validaCidadePossuiBairro(UUID cidadeId, UUID bairroId) {
        var cidade = getCidadePorId(cidadeId);
        var bairro = getBairroPorId(bairroId);

        if (!bairroRepository.existsByIdAndCidadeId(bairro.getId(), cidade.getId())) {
            return false;
        } else {
            return true;
        }

    }

    public Cidade getCidadePorId(UUID cidadeId) {
        return cidadeRepository.findById(cidadeId)
                .orElseThrow(() -> new ConflitoEntidadeInexistente("Cidade não encontrada"));
    }

    public Bairro getBairroPorId(UUID bairroId) {
        return bairroRepository.findById(bairroId)
                .orElseThrow(() -> new ConflitoEntidadeInexistente("Bairro não encontrado"));
    }


}
