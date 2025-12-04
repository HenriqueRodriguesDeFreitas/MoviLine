package com.vortex.EntregaRapida.service.validation;

import com.vortex.EntregaRapida.model.Bairro;

import java.util.List;

public final class BairroValidator {

    public static boolean cidadePossuiBairro(String nome,List<Bairro> bairros){
        return bairros.stream()
                .anyMatch(b -> b.getNome().equalsIgnoreCase(nome));
    }
}
