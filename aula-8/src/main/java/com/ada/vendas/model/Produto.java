package com.ada.vendas.model;

import java.util.Objects;

public class Produto {

    private Long id;
    private String nome;
    private String categoria;

    public Produto(Long id, String nome, String categoria) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Produto produto)) return false;

        return Objects.equals(id, produto.id);
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
