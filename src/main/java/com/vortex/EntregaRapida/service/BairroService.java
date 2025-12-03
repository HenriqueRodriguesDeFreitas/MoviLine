package com.vortex.EntregaRapida.service;

import com.vortex.EntregaRapida.mapper.BairroMapper;
import com.vortex.EntregaRapida.repository.BairroRepository;
import com.vortex.EntregaRapida.repository.CidadeRepository;
import org.springframework.stereotype.Service;

@Service
public class BairroService {

    private final BairroRepository bairroRepository;
    private final CidadeRepository cidadeRepository;
    private final BairroMapper bairroMapper;

    public BairroService(BairroRepository bairroRepository, CidadeRepository cidadeRepository, BairroMapper bairroMapper) {
        this.bairroRepository = bairroRepository;
        this.cidadeRepository = cidadeRepository;
        this.bairroMapper = bairroMapper;
    }
}
