package com.ada.vendas.model;

import java.util.Objects;

public class Cliente {

    private Long id;
    private String nome;
    private String email;

    public Cliente(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Cliente cliente)) return false;

        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return id + " - " + nome;
    }
}
