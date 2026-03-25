package com.ada.vendas.model;

import java.time.LocalDate;
import java.util.Objects;

public class Venda {

    private Long id;
    private Cliente cliente;
    private Produto produto;
    private Integer quantidade;
    private Double valorUnitario;
    private LocalDate dataDaVenda;

    public Venda(Long id, Cliente cliente, Produto produto,
                 Integer quantidade, Double valorUnitario,
                 LocalDate dataDaVenda) {

        this.id = id;
        this.cliente = cliente;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.dataDaVenda = dataDaVenda;
    }

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Produto getProduto() { return produto; }
    public Integer getQuantidade() { return quantidade; }
    public Double getValorUnitario() { return valorUnitario; }
    public LocalDate getDataDaVenda() { return dataDaVenda; }

    public Double getValorTotal() {
        return quantidade * valorUnitario;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Venda venda)) return false;

        return Objects.equals(id, venda.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return id + " - " + cliente.getNome() + " - " + produto.getNome() + " - " + quantidade + " - " + valorUnitario + " - " + dataDaVenda;
    }
}
