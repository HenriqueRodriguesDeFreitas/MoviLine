package com.vortex.EntregaRapida.docs;

public class ErroExamples {
    public static final String ERRO_404 = """
            {
              "data_hora": "2025-09-01T12:00:00",
              "httpsValue": 404,
              "erro": "Não encontrado",
              "descricao": "Recurso solicitado não existe"
            }
            """;

    public static final String ERRO_409 = """
            {
              "data_hora": "2025-09-01T12:00:00",
              "httpsValue": 409,
              "erro": "Conflito",
              "descricao": "Já existe um recurso com esses dados"
            }
            """;
}
