package com.vortex.EntregaRapida.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cidade")
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "nome", length = 32, nullable = false)
    private String nome;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;

    @OneToMany(mappedBy = "cidade", orphanRemoval = true)
    private List<Bairro> bairros;

    public Cidade() {
    }

    public Cidade(String nome, Estado estado) {
        if (estado == null) throw new IllegalArgumentException("Uma cidade deve pertencer a um estado.");
        this.nome = nome;
        this.estado = estado;
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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public boolean validarCidadePertenceEstado(Estado estado) {
        if (this.estado == null || estado == null) {
            return false;
        }
        return this.estado.getId().equals(estado.getId());
    }
}
