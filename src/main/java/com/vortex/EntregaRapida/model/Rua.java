package com.vortex.EntregaRapida.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "rua")
public class Rua {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "rua", length = 250, nullable = false)
    private String nome;

    @Column(name = "cep", length = 8, nullable = false)
    private String cep;

    @ManyToOne
    @JoinColumn(name = "bairro_id", nullable = false)
    private Bairro bairro;

    public Rua() {
    }

    public Rua(UUID id, String nome, String cep, Bairro bairro) {
        this.id = id;
        this.nome = nome;
        this.cep = cep;
        this.bairro = bairro;
    }

    public Rua(String nome, String cep, Bairro bairro) {
        this.nome = nome;
        this.cep = cep;
        this.bairro = bairro;
    }


    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }
}
