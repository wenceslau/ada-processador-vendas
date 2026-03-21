package com.ada.vendas;

import com.ada.vendas.model.Produto;
import com.ada.vendas.model.Venda;

import java.util.*;
import java.util.stream.Collectors;

public class ReviewStreams {

    public static void main(String[] args) {

        List<Venda> vendas = gerarVendasFake();

        /*------------------------------------------------------------------------------------------------*/
        //📌 1. stream() básico
        System.out.println("Todas as vendas:");

        vendas.stream()
                .forEach(System.out::println);

        /*------------------------------------------------------------------------------------------------*/
        //📌 2. filter()
        System.out.println("\nVendas acima de 1000:");

        vendas.stream()
                .filter(v -> v.getValorTotal() > 1000)
                .forEach(System.out::println);

        /*------------------------------------------------------------------------------------------------*/
        //📌 3. map()
        System.out.println("\nNome dos produtos:");

        vendas.stream()
                .map(v -> v.getProduto().getNome())
                .forEach(System.out::println);

        /*------------------------------------------------------------------------------------------------*/
        //📌 4. map + collect
        System.out.println("\nLista de nomes:");

        List<String> nomes =
                vendas.stream()
                        .map(v -> v.getProduto().getNome())
                        .collect(Collectors.toList());

        nomes.forEach(System.out::println);

        /*------------------------------------------------------------------------------------------------*/
        //📌 5. sorted()
        System.out.println("\nOrdenado por valor:");

        vendas.stream()
                .sorted(Comparator.comparing(Venda::getValorTotal))
                .forEach(System.out::println);

        /*------------------------------------------------------------------------------------------------*/
        //📌 6. mapToDouble + sum
        System.out.println("\nTotal vendido:");

        double total =
                vendas.stream()
                        .mapToDouble(Venda::getValorTotal)
                        .sum();

        System.out.println(total);

        /*------------------------------------------------------------------------------------------------*/
        //📌 7. groupingBy
        System.out.println("\nAgrupado por cliente:");

        Map<String, List<Venda>> porCliente =
                vendas.stream()
                        .collect(Collectors.groupingBy(
                                v -> v.getCliente().getNome()
                        ));

        porCliente.forEach((cliente, lista) -> {
            System.out.println(cliente);
            lista.forEach(v -> System.out.println("  " + v));
        });

        /*------------------------------------------------------------------------------------------------*/
        //📌 8. groupingBy + summingDouble
        System.out.println("\nTotal por cliente:");

        Map<String, Double> totalPorCliente =
                vendas.stream()
                        .collect(Collectors.groupingBy(
                                v -> v.getCliente().getNome(),
                                Collectors.summingDouble(Venda::getValorTotal)
                        ));

        totalPorCliente.forEach((cliente, valor) ->
                System.out.println(cliente + " -> " + valor)
        );

        /*------------------------------------------------------------------------------------------------*/
        //📌 9. distinct()
        System.out.println("\nCategorias únicas:");

        vendas.stream()
                .map(v -> v.getProduto().getCategoria())
                .distinct()
                .forEach(System.out::println);

        /*------------------------------------------------------------------------------------------------*/
        //📌 10. limit()
        System.out.println("\nPrimeiras 2 vendas:");

        vendas.stream()
                .limit(2)
                .forEach(System.out::println);

        /*------------------------------------------------------------------------------------------------*/
        //📌 11. findFirst()
        System.out.println("\nPrimeira venda acima de 1000:");

        vendas.stream()
                .filter(v -> v.getValorTotal() > 1000)
                .findFirst()
                .ifPresent(System.out::println);

        /*------------------------------------------------------------------------------------------------*/
        //📌 12. anyMatch()
        System.out.println("\nExiste venda acima de 3000?");

        boolean existe =
                vendas.stream()
                        .anyMatch(v -> v.getValorTotal() > 3000);

        System.out.println(existe);

        /*------------------------------------------------------------------------------------------------*/
        //📌 13. pipeline completo
        System.out.println("\nPipeline completo:");

        vendas.stream()
                .filter(v -> v.getValorTotal() > 1000)
                .map(v -> v.getProduto().getNome())
                .sorted()
                .forEach(System.out::println);
    }

    /*------------------------------------------------------------------------------------------------*/
    private static List<Venda> gerarVendasFake() {

        List<Venda> vendas = new ArrayList<>();

        vendas.add(new Venda(1L, new com.ada.vendas.model.Cliente(null, "Maria", ""),
                new Produto(null, "Notebook", "Eletronicos"), 1, 3500.0, null));

        vendas.add(new Venda(2L, new com.ada.vendas.model.Cliente(null, "Joao", ""),
                new Produto(null, "Mouse", "Eletronicos"), 2, 120.0, null));

        vendas.add(new Venda(3L, new com.ada.vendas.model.Cliente(null, "Ana", ""),
                new Produto(null, "Cadeira", "Moveis"), 1, 800.0, null));

        vendas.add(new Venda(4L, new com.ada.vendas.model.Cliente(null, "Maria", ""),
                new Produto(null, "Mesa", "Moveis"), 1, 1500.0, null));

        return vendas;
    }
}
