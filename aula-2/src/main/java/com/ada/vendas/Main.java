package com.ada.vendas;

import com.ada.vendas.model.Cliente;
import com.ada.vendas.model.Produto;
import com.ada.vendas.model.Venda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {

        List<Venda> vendas = gerarVendasExemplo();

        LocalDate data = LocalDate.now().minusDays(7);
        List<Venda> vendasAposData = filtrarVendas(vendas, v -> v.getDataVenda().isAfter(data));
        imprimirVendas(vendasAposData);

        List<Venda> vendasCaras = filtrarVendas(vendas, v -> v.getValorTotal() > 1000);
        imprimirVendas(vendasCaras);

        LocalDate inicio = LocalDate.now().minusDays(7);
        LocalDate fim = LocalDate.now();
        Predicate<Venda> regra = v ->
                (v.getDataVenda().isEqual(inicio) || v.getDataVenda().isAfter(inicio)) &&
                (v.getDataVenda().isEqual(fim) || v.getDataVenda().isBefore(fim));
        List<Venda> vendasEntreDatas = filtrarVendas(vendas, regra);
        imprimirVendas(vendasEntreDatas);


        List<Venda> vendasDoMesAtual = filtrarVendas(
                vendas,
                v -> v.getDataVenda().getMonth() == LocalDate.now().getMonth() &&
                     v.getDataVenda().getYear() == LocalDate.now().getYear()
        );
        imprimirVendas(vendasDoMesAtual);


        List<Venda> vendasUltimos7Dias = filtrarVendas(vendas, regraVendasUltimos7Dias());
        imprimirVendas(vendasUltimos7Dias);


        List<Venda> vendasAnoAtual = filtrarVendas(vendas, regraVendasAnoAtual());
        imprimirVendas(vendasAnoAtual);


        // X-POINT Exercicios
        List<Venda> vendasQuantidadeMaior2 =
                filtrarVendas(vendas, v -> v.getQuantidade() > 2);
        imprimirVendas(vendasQuantidadeMaior2);


        LocalDate hoje = LocalDate.now();
        List<Venda> vendasMesAtual2 =
                filtrarVendas(vendas,
                        v -> v.getDataVenda().getMonth() == hoje.getMonth()
                             && v.getDataVenda().getYear() == hoje.getYear());
        imprimirVendas(vendasMesAtual2);


        List<Venda> vendasEletronicos =
                filtrarVendas(vendas,
                        v -> v.getProduto().getCategoria().equals("Eletrônicos"));
        imprimirVendas(vendasEletronicos);


        List<Venda> vendasRecentesEValorAlto =
                filtrarVendas(vendas,
                        v -> v.getValorTotal() > 500 &&
                             v.getDataVenda().isAfter(LocalDate.now().minusDays(30)));
        imprimirVendas(vendasRecentesEValorAlto);

    }

    public static List<Venda> gerarVendasExemplo() {

        List<Venda> vendas = new ArrayList<>();

        Cliente cliente1 = new Cliente(1L, "Maria", "maria@email.com");
        Cliente cliente2 = new Cliente(2L, "João", "joao@email.com");
        Cliente cliente3 = new Cliente(3L, "Ana", "ana@email.com");

        Produto produto1 = new Produto(1L, "Notebook", "Eletrônicos");
        Produto produto2 = new Produto(2L, "Mouse", "Eletrônicos");
        Produto produto3 = new Produto(3L, "Teclado", "Eletrônicos");
        Produto produto4 = new Produto(4L, "Cadeira", "Móveis");

        vendas.add(new Venda(1L, cliente1, produto1, 1, 3500.0, LocalDate.now().minusDays(2)));
        vendas.add(new Venda(2L, cliente2, produto2, 2, 120.0, LocalDate.now().minusDays(10)));
        vendas.add(new Venda(3L, cliente3, produto3, 1, 200.0, LocalDate.now().minusDays(30)));
        vendas.add(new Venda(4L, cliente1, produto4, 1, 800.0, LocalDate.now().minusMonths(1)));
        vendas.add(new Venda(5L, cliente2, produto1, 1, 3500.0, LocalDate.now().minusDays(5)));
        vendas.add(new Venda(6L, cliente3, produto2, 3, 120.0, LocalDate.now().minusDays(1)));
        vendas.add(new Venda(7L, cliente1, produto3, 2, 200.0, LocalDate.now().minusMonths(2)));
        vendas.add(new Venda(8L, cliente2, produto4, 1, 900.0, LocalDate.now().minusDays(15)));
        vendas.add(new Venda(9L, cliente3, produto1, 1, 3500.0, LocalDate.now().minusDays(7)));
        vendas.add(new Venda(10L, cliente1, produto2, 4, 120.0, LocalDate.now().minusDays(3)));

        return vendas;
    }

    public static List<Venda> filtrarVendas(List<Venda> vendas, Predicate<Venda> regra) {

        List<Venda> resultado = new ArrayList<>();

        for (Venda venda : vendas) {
            if (regra.test(venda)) {
                resultado.add(venda);
            }
        }

        return resultado;
    }

    public static Predicate<Venda> regraVendasUltimos7Dias() {

        LocalDate hoje = LocalDate.now();
        LocalDate seteDiasAtras = hoje.minusDays(7);

        return v -> v.getDataVenda().isEqual(seteDiasAtras) || v.getDataVenda().isAfter(seteDiasAtras);

    }

    public static Predicate<Venda> regraVendasAnoAtual() {

        int anoAtual = LocalDate.now().getYear();

        return v -> v.getDataVenda().getYear() == anoAtual;
    }

    /*
    TODO - Exercício 1
           Usando filtrarVendas, criar filtro para:
           Vendas com quantidade maior que 2.

    TODO -  Exercício 2
            Criar filtro para:
            Vendas do mês atual.

    TODO - Exercício 3
           Criar filtro para:
           Vendas de produtos da categoria Eletrônicos.

    TODO - Exercício 4 (Desafio)
           Criar filtro combinando duas condições:
           valor maior que 500
           venda realizada nos últimos 30 dias
     */

    public static void imprimirVendas(List<Venda> vendas) {
        vendas.forEach(v -> System.out.println(v));
    }

}
