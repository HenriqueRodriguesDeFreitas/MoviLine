package com.vortex.EntregaRapida.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CidadeTest {

    private Estado estado;
    private Cidade cidade;
    private Bairro bairro;

    @BeforeEach
    void setUp() {
        UUID id = UUID.randomUUID();
        estado = new Estado("Pará");
        estado.setId(id);
        cidade = new Cidade("Breves", estado);
        cidade.setId(id);

        estado.setCidades(List.of(cidade));

        bairro = new Bairro(UUID.randomUUID(), "Aeroporto", cidade);
        cidade.setBairros(List.of(bairro));
    }

    @Test
    void validarCidadeJaPertenceEstado_casoCidadeJaPerteceAoEstado_deveRetornarTrue() {
        boolean resultado = cidade.validarCidadeJaPertenceEstado(estado);
        assertTrue(resultado, "Cidade deve pertencer ao estado");
    }

    @Test
    void validarCidadeJaPertenceEstado_quandoCidadeNaoPerteceAoEstado_deveRetornarFalse() {
        Cidade cidadeNovo = new Cidade("Palhoça", estado);
        cidadeNovo.setId(UUID.randomUUID());
        boolean resultado = cidadeNovo.validarCidadeJaPertenceEstado(estado);
        assertFalse(resultado, "Cidade ainda não pertence a este estado");
    }

    @Test
    void verificarCidadeJaPossuiBairro_casoCidadeJaPossuaBairro_deveRetornarTrue() {
        boolean resultado = cidade.verificarCidadeJaPossuiBairro(bairro.getNome());
        assertTrue(resultado, "Cidade deve possuir este bairro.");
    }

    @Test
    void verificarCidadeJaPossuiBairro_casoCidadeNaoPossuaBairro_deveRetornarFalse() {
        boolean resultado = cidade.verificarCidadeJaPossuiBairro("Centro");
        assertFalse(resultado, "Cidade ainda não possui este bairro.");
    }
}