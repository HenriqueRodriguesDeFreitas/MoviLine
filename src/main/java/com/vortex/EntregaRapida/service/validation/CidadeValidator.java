package com.vortex.EntregaRapida.service.validation;

import com.vortex.EntregaRapida.model.Cidade;

import java.util.List;


public final class CidadeValidator {

    public CidadeValidator() {
    }

    /*Usar para o cadastro de uma nova cidade
     * Verifica se estado já possui uma cidade com nome passado.*/
    public static boolean estadoPossuiCidade(String nomeNovaCidade, List<Cidade> cidades) {
        return cidades.stream()
                .anyMatch(c -> c.getNome().equalsIgnoreCase(nomeNovaCidade));
    }

    /*Usar para atualizar dados de uma cidade
     * Verifica se os ids são diferentes, mas nomes são iguais.*/
    public static boolean estadoPossuiCidade(String novoNome, List<Cidade> cidades,
                                             Cidade cidadeAtual) {
        return cidades.stream()
                .anyMatch(c -> !c.getId().equals(cidadeAtual.getId())
                        && c.getNome().equalsIgnoreCase(novoNome));
    }

    /*Usar para metodo de deletar
    * Verifica se o estado já possui uma cidade com o id passado*/
    public static boolean estadoPossuiCidade(UUID cidadeId, List<Cidade> cidades) {
        return cidades.stream()
                .anyMatch(c -> c.getId().equals(cidadeId));
    }
}
