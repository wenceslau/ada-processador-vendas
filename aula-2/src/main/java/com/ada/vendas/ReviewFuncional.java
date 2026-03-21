package com.ada.vendas;

import com.ada.vendas.model.Venda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ReviewFuncional {

    public static void main(String[] args) {

        List<Venda> vendas = gerarVendasFake();

        /*------------------------------------------------------------------------------------------------*/
        //📌 1. Forma tradicional (imperativa)
        System.out.println("Vendas acima de 1000 (forma tradicional):");

        for (Venda v : vendas) {
            if (v.getValorTotal() > 1000) {
                System.out.println(v);
            }
        }

        /*------------------------------------------------------------------------------------------------*/
        //📌 2. Usando Predicate
        System.out.println("\nUsando Predicate:");

        Predicate<Venda> valorMaiorQueMil =
                v -> v.getValorTotal() > 1000;

        vendas.stream()
                .filter(valorMaiorQueMil)
                .forEach(System.out::println);

        /*------------------------------------------------------------------------------------------------*/
        //📌 3. Lambda direto no filter
        System.out.println("\nLambda direto:");

        vendas.stream()
                .filter(v -> v.getValorTotal() > 1000)
                .forEach(System.out::println);

        /*------------------------------------------------------------------------------------------------*/
        //📌 4. Passando comportamento como parâmetro
        System.out.println("\nMétodo genérico com Predicate:");

        filtrarVendas(vendas, v -> v.getValorTotal() > 1000);

        /*------------------------------------------------------------------------------------------------*/
        //📌 5. Reutilizando regras
        System.out.println("\nRegras reutilizáveis:");

        Predicate<Venda> categoriaEletronicos =
                v -> "Eletronicos".equals(v.getProduto().getCategoria());

        vendas.stream()
                .filter(valorMaiorQueMil)
                .filter(categoriaEletronicos)
                .forEach(System.out::println);

        /*------------------------------------------------------------------------------------------------*/
        //📌 6. Combinando Predicate
        System.out.println("\nCombinando regras (and):");

        Predicate<Venda> regraCombinada =
                valorMaiorQueMil.and(categoriaEletronicos);

        vendas.stream()
                .filter(regraCombinada)
                .forEach(System.out::println);

        /*------------------------------------------------------------------------------------------------*/
        //📌 7. Exemplo com or
        System.out.println("\nCombinando regras (or):");

        Predicate<Venda> valorAltoOuEletronico =
                valorMaiorQueMil.or(categoriaEletronicos);

        vendas.stream()
                .filter(valorAltoOuEletronico)
                .forEach(System.out::println);
    }

    /*------------------------------------------------------------------------------------------------*/
    // Metodo genérico
    public static void filtrarVendas(List<Venda> vendas, Predicate<Venda> regra) {

        vendas.stream()
                .filter(regra)
                .forEach(System.out::println);
    }

    /*------------------------------------------------------------------------------------------------*/
    // Dados fake
    private static List<Venda> gerarVendasFake() {

        List<Venda> vendas = new ArrayList<>();

        vendas.add(new Venda(1L, null,
                new com.ada.vendas.model.Produto(null, "Notebook", "Eletronicos"),
                1, 3500.0, null));

        vendas.add(new Venda(2L, null,
                new com.ada.vendas.model.Produto(null, "Mouse", "Eletronicos"),
                2, 120.0, null));

        vendas.add(new Venda(3L, null,
                new com.ada.vendas.model.Produto(null, "Cadeira", "Moveis"),
                1, 800.0, null));

        vendas.add(new Venda(4L, null,
                new com.ada.vendas.model.Produto(null, "Mesa", "Moveis"),
                1, 1500.0, null));

        return vendas;
    }
}
