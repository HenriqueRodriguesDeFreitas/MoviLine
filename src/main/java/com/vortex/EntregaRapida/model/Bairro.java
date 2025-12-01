package com.vortex.EntregaRapida.model;

import com.vortex.EntregaRapida.exception.custom.ConflitoDeEntidadeException;
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
        if (nome == null) throw new IllegalArgumentException("Nome do bairro não pode ser nulo");
        if (cidade == null) throw new IllegalArgumentException("Bairro precisa ser vinculado a uma cidade");
        this.id = id;
        this.nome = nome;
        this.cidade = cidade;
    }

    public Bairro(String nome, Cidade cidade) {
        if (nome == null) throw new IllegalArgumentException("Nome do bairro não pode ser nulo");
        if (cidade == null) throw new ConflitoDeEntidadeException("Bairro precisa ser vinculado a uma cidade");
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
        if (nome == null) throw new IllegalArgumentException("Nome do bairro não pode ser nulo");
        this.nome = nome;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        if (cidade == null) throw new IllegalArgumentException("Bairro precisa ser vinculado a uma cidade");
        this.cidade = cidade;
    }
}
