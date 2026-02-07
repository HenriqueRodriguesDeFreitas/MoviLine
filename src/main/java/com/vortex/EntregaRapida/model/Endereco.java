package com.vortex.EntregaRapida.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "numero")
    private int numero;

    @Column(name = "complemento", length = 250)
    private String complemento;

    @ManyToOne
    @JoinColumn(name = "rua_id", nullable = false)
    private Rua rua;

    public Endereco() {
    }

    public Endereco(UUID id, int numero, String complemento, Rua rua) {
        this.id = id;
        this.numero = numero;
        this.complemento = complemento;
        this.rua = rua;
    }

    public Endereco(int numero, String complemento, Rua rua) {
        this.numero = numero;
        this.complemento = complemento;
        this.rua = rua;
    }

    public UUID getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Rua getRua() {
        return rua;
    }

    public void setRua(Rua rua) {
        this.rua = rua;
    }
}
