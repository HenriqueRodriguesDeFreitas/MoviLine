package com.vortex.EntregaRapida.service.validation;

import com.vortex.EntregaRapida.model.Cidade;

import java.util.List;


public final class CidadeValidator {

    public CidadeValidator() {
    }

    //Usar para o cadastro de uma nova cidade
    public static boolean estadoPossuiCidade(String nomeNovaCidade, List<Cidade> cidades) {
        return cidades.stream()
                .anyMatch(c -> c.getNome().equalsIgnoreCase(nomeNovaCidade));
    }

    //Usar para atualizar dados de uma cidade
    public static boolean estadoPossuiCidade(String novoNome, List<Cidade> cidades,
                                             Cidade cidadeAtual) {
        return cidades.stream()
                .anyMatch(c -> !c.getId().equals(cidadeAtual.getId())
                        && c.getNome().equalsIgnoreCase(novoNome));
    }
}
