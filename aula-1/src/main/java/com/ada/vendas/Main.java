package com.ada.vendas;

import com.ada.vendas.model.Cliente;
import com.ada.vendas.model.Produto;
import com.ada.vendas.model.Venda;

import java.time.temporal.ChronoUnit;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Cliente cliente1 = new Cliente(1L, "Maria", "maria@email.com");
        Cliente cliente2 = new Cliente(2L, "João", "joao@email.com");

        Produto produto1 = new Produto(1L, "Notebook", "Eletrônicos");
        Produto produto2 = new Produto(2L, "Mouse", "Eletrônicos");

        List<Venda> vendas = new ArrayList<>();

        vendas.add(new Venda(1L, cliente1, produto1, 1, 3500.0,
                LocalDate.now().minusDays(2)));

        vendas.add(new Venda(2L, cliente2, produto2, 2, 100.0,
                LocalDate.now().minusMonths(1)));

        vendas.add(new Venda(3L, cliente1, produto2, 3, 100.0,
                LocalDate.now().minusDays(10)));

        // Chamando métodos
        listarVendasDoMesAtual(vendas);

    }

    public static List<Venda> buscarVendasAposData(List<Venda> vendas, LocalDate data) {

        List<Venda> resultado = new ArrayList<>();

        for (Venda venda : vendas) {
            if (venda.getDataVenda().isAfter(data)) {
                resultado.add(venda);
            }
        }

        return resultado;
    }

    public static List<Venda> buscarVendasEntreDatas(List<Venda> vendas, LocalDate inicio, LocalDate fim) {

        List<Venda> resultado = new ArrayList<>();

        for (Venda venda : vendas) {
            LocalDate dataVenda = venda.getDataVenda();

            if ((dataVenda.isEqual(inicio) || dataVenda.isAfter(inicio)) &&
                (dataVenda.isEqual(fim) || dataVenda.isBefore(fim))) {

                resultado.add(venda);
            }
        }

        return resultado;
    }

    public static void listarVendasDoMesAtual(List<Venda> vendas) {

        LocalDate hoje = LocalDate.now();

        for (Venda venda : vendas) {
            if (venda.getDataVenda().getMonth() == hoje.getMonth()
                && venda.getDataVenda().getYear() == hoje.getYear()) {

                System.out.println("Venda ID: " + venda.getId()
                                   + " | Data: " + venda.getDataVenda()
                                   + " | Total: " + venda.getValorTotal());
            }
        }
    }

    public static void calcularDiasDesdeVenda(List<Venda> vendas) {

        LocalDate hoje = LocalDate.now();

        for (Venda venda : vendas) {
            long dias = ChronoUnit.DAYS.between(venda.getDataVenda(), hoje);

            System.out.println("Venda ID: " + venda.getId()
                               + " ocorreu há " + dias + " dias.");
        }
    }

    /*
    📝 Exercício 1
       TODO: Criar um método que retorne todas as vendas realizadas nos últimos 7 dias.

    📝 Exercício 2
       TODO: Criar um método que retorne todas as vendas do ano atual.

    📝 Exercício 3
       TODO: Criar um método que calcule o total vendido em determinado período.
            (Parâmetros: data início e data fim)

       Exercício 4 (Desafio)
       TODO: Criar um método que retorne a venda mais antiga da lista.
     */

    public static List<Venda> buscarVendasUltimos7Dias(List<Venda> vendas) {

        List<Venda> resultado = new ArrayList<>();
        LocalDate hoje = LocalDate.now();
        LocalDate seteDiasAtras = hoje.minusDays(7);

        for (Venda venda : vendas) {
            LocalDate dataVenda = venda.getDataVenda();

            if ((dataVenda.isEqual(seteDiasAtras) || dataVenda.isAfter(seteDiasAtras))
                && (dataVenda.isBefore(hoje) || dataVenda.isEqual(hoje))) {

                resultado.add(venda);
            }
        }

        return resultado;
    }

    public static List<Venda> buscarVendasAnoAtual(List<Venda> vendas) {

        List<Venda> resultado = new ArrayList<>();
        int anoAtual = LocalDate.now().getYear();

        for (Venda venda : vendas) {
            if (venda.getDataVenda().getYear() == anoAtual) {
                resultado.add(venda);
            }
        }

        return resultado;
    }

    public static double calcularTotalVendidoPeriodo(List<Venda> vendas, LocalDate inicio, LocalDate fim) {

        double total = 0.0;

        for (Venda venda : vendas) {
            LocalDate dataVenda = venda.getDataVenda();

            if ((dataVenda.isEqual(inicio) || dataVenda.isAfter(inicio)) &&
                (dataVenda.isEqual(fim) || dataVenda.isBefore(fim))) {

                total += venda.getValorTotal();
            }
        }

        return total;
    }

    public static Venda buscarVendaMaisAntiga(List<Venda> vendas) {

        if (vendas.isEmpty()) {
            return null;
        }

        Venda vendaMaisAntiga = vendas.get(0);

        for (Venda venda : vendas) {
            if (venda.getDataVenda().isBefore(vendaMaisAntiga.getDataVenda())) {
                vendaMaisAntiga = venda;
            }
        }

        return vendaMaisAntiga;
    }
}
