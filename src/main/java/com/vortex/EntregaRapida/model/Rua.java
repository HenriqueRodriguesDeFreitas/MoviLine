package com.vortex.EntregaRapida.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "rua", length = 250, nullable = false)
    private String rua;

    @Column(name = "cep", length = 9, nullable = false)
    private String cep;

    @OneToOne
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;

    @OneToOne
    @JoinColumn(name = "cidade_id", nullable = false)
    private Cidade cidade;

    @OneToOne
    @JoinColumn(name = "bairro_id", nullable = false)
    private Bairro bairro;
}
