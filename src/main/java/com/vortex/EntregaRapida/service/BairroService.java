package com.vortex.EntregaRapida.service;

import com.vortex.EntregaRapida.dto.request.BairroRequestDto;
import com.vortex.EntregaRapida.dto.response.BairroResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoEntidadeInexistente;
import com.vortex.EntregaRapida.mapper.BairroMapper;
import com.vortex.EntregaRapida.model.Bairro;
import com.vortex.EntregaRapida.repository.BairroRepository;
import com.vortex.EntregaRapida.service.validation.BairroCidadeValidator;
import com.vortex.EntregaRapida.service.validation.CidadeEstadoValidation;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BairroService {

    private final BairroRepository bairroRepository;
    private final CidadeEstadoValidation cidadeEstadoValidation;
    private final BairroCidadeValidator bairroCidadeValidator;
    private final BairroMapper bairroMapper;

    public BairroService(BairroRepository bairroRepository,
                         BairroMapper bairroMapper,
                         BairroCidadeValidator bairroCidadeValidator,
                         CidadeEstadoValidation cidadeEstadoValidation) {
        this.bairroRepository = bairroRepository;
        this.bairroMapper = bairroMapper;
        this.cidadeEstadoValidation = cidadeEstadoValidation;
        this.bairroCidadeValidator = bairroCidadeValidator;
    }

    @Transactional
    public BairroResponseDto cadastrarBairro(BairroRequestDto dto) {
        if (!cidadeEstadoValidation
                .validaCidadePertenceAoEstado(dto.cidadeId(), dto.estadoId())) {
            throw new ConflitoEntidadeInexistente("O estado não possui a cidade pesquisada.");
        }


        if (bairroCidadeValidator
                .cidadePossuiBairroComNomePassado(dto.nome(), dto.cidadeId())) {
            throw new ConflitoEntidadeInexistente("Esta cidade já possui bairro com mesmo nome");
        }

        var cidade = cidadeEstadoValidation.validaCidadePorId(dto.cidadeId());
        Bairro novoBairro = new Bairro(dto.nome(), cidade);
        return bairroMapper.toResponse(bairroRepository.save(novoBairro));
    }

    @Transactional
    public BairroResponseDto atualizarBairro(UUID bairroId,
                                             BairroRequestDto dto) {
        return bairroMapper.toResponse(new Bairro(UUID.randomUUID(), dto.nome()));

    }
}
