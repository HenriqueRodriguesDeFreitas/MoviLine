package com.vortex.EntregaRapida.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "bairro")
public class Bairro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 150, nullable = false)
    private String nome;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cidade_id", nullable = false)
    private Cidade cidade;

    public Bairro() {
    }

    public Bairro(UUID id, String nome, Cidade cidade) {
        if(nome == null) throw new IllegalArgumentException("");
        this.id = id;
        this.nome = nome;
        this.cidade = cidade;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }
}
