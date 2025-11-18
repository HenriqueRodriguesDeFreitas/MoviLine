package com.vortex.EntregaRapida.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", length = 19, nullable = false, unique = true)
    private String nome;

    @OneToMany(mappedBy = "estado", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Cidade> cidades;

    public Estado(){}

    public Estado(String nome) {
        this.nome = nome;
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

    public List<Cidade> getCidades() {
        return cidades;
    }

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }
}
