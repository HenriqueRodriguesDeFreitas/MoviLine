package com.vortex.EntregaRapida.service;

import com.vortex.EntregaRapida.dto.request.BairroPorIdRequestDto;
import com.vortex.EntregaRapida.dto.request.BairroRequestDto;
import com.vortex.EntregaRapida.dto.response.BairroResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoDeEntidadeException;
import com.vortex.EntregaRapida.exception.custom.ConflitoEntidadeInexistente;
import com.vortex.EntregaRapida.mapper.BairroMapper;
import com.vortex.EntregaRapida.model.Bairro;
import com.vortex.EntregaRapida.repository.BairroRepository;
import com.vortex.EntregaRapida.service.validation.BairroCidadeValidator;
import com.vortex.EntregaRapida.service.validation.CidadeEstadoValidator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BairroService {

    private final BairroRepository bairroRepository;
    private final CidadeEstadoValidator cidadeEstadoValidation;
    private final BairroCidadeValidator bairroCidadeValidator;
    private final BairroMapper bairroMapper;
    private static final String MSG_CIDADE_NAO_POSSUI_BAIRRO = "Esta cidade não possui o bairro informado.";

    public BairroService(BairroRepository bairroRepository,
                         BairroMapper bairroMapper,
                         BairroCidadeValidator bairroCidadeValidator,
                         CidadeEstadoValidator cidadeEstadoValidation) {
        this.bairroRepository = bairroRepository;
        this.bairroMapper = bairroMapper;
        this.cidadeEstadoValidation = cidadeEstadoValidation;
        this.bairroCidadeValidator = bairroCidadeValidator;
    }

    @Transactional
    public BairroResponseDto cadastrarBairro(BairroRequestDto dto) {
        verificaCidadePertenceAoEstado(dto);

        verificaCidadePossuiBairroComMesmoNome(dto);

        var cidade = cidadeEstadoValidation.validaCidadePorId(dto.cidadeId());
        Bairro novoBairro = new Bairro(dto.nome(), cidade);
        return bairroMapper.toResponse(bairroRepository.save(novoBairro));
    }

    @Transactional
    public BairroResponseDto atualizarBairro(UUID bairroId,
                                             BairroRequestDto dto) {
        verificaCidadePertenceAoEstado(dto);

        var bairroEncontrado = getBairroPorId(bairroId);

        if (!bairroEncontrado.getNome().equals(dto.nome())) {
            verificaCidadePossuiBairroComMesmoNome(dto);
        }

        var cidadeEncontrada = cidadeEstadoValidation.validaCidadePorId(dto.cidadeId());

        bairroEncontrado.setNome(dto.nome());
        bairroEncontrado.setCidade(cidadeEncontrada);

        return bairroMapper.toResponse(bairroRepository.save(bairroEncontrado));
    }

    public BairroResponseDto buscarPorId(BairroPorIdRequestDto dto) {
        cidadeEstadoValidation.validaCidadePertenceAoEstado(dto.cidadeId(), dto.estadoId());

        if (!bairroRepository.existsByIdAndCidadeId(dto.bairroId(), dto.cidadeId())) {
            throw new ConflitoEntidadeInexistente(MSG_CIDADE_NAO_POSSUI_BAIRRO);
        }

        var bairroEncontrado = getBairroPorId(dto.bairroId());
        return bairroMapper.toResponse(bairroEncontrado);
    }

    public BairroResponseDto buscarBairroPorNome(BairroRequestDto dto){
        verificaCidadePertenceAoEstado(dto);

        var bairroEncontrado = bairroRepository
                .findByNomeIgnoreCaseAndCidadeId(dto.nome(), dto.cidadeId())
                .orElseThrow(()-> new ConflitoEntidadeInexistente(MSG_CIDADE_NAO_POSSUI_BAIRRO));

        return bairroMapper.toResponse(bairroEncontrado);
    }

    public List<BairroResponseDto> buscarBairrosDeCidade(UUID estadoId, UUID cidadeId) {
        verificaCidadePertenceAoEstado(estadoId, cidadeId);
        List<Bairro> bairros = bairroRepository.findByCidadeId(cidadeId);
        return bairros.stream()
                .map(bairroMapper::toResponse).toList();
    }

    private void verificaCidadePossuiBairroComMesmoNome(BairroRequestDto dto) {
        if (bairroCidadeValidator
                .cidadePossuiBairroComNomePassado(dto.nome(), dto.cidadeId())) {
            throw new ConflitoDeEntidadeException("Esta cidade já possui bairro com mesmo nome.");
        }
    }

    private void verificaCidadePertenceAoEstado(BairroRequestDto dto) {
        if (!cidadeEstadoValidation
                .validaCidadePertenceAoEstado(dto.cidadeId(), dto.estadoId())) {
            throw new ConflitoEntidadeInexistente("O estado não possui a cidade pesquisada.");
        }
    }

    private void verificaCidadePertenceAoEstado(UUID estadoId, UUID cidadeId) {
        if (!cidadeEstadoValidation
                .validaCidadePertenceAoEstado(cidadeId, estadoId)) {
            throw new ConflitoEntidadeInexistente("O estado não possui a cidade pesquisada.");
        }
    }

    private Bairro getBairroPorId(UUID bairroId) {
        return bairroRepository.findById(bairroId).orElseThrow(
                () -> new ConflitoEntidadeInexistente("Bairro não encontrado com ID: " + bairroId));
    }
}
