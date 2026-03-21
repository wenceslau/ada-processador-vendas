package com.ada.vendas;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.Consumer;

public class ReviewFuncionalInterfacesOptional {

    public static void main(String[] args) {

        /*------------------------------------------------------------------------------------------------*/
        //📌 1. Interface Funcional (conceito)
        System.out.println("Interface Funcional:");

        Operacao soma = (a, b) -> a + b;
        System.out.println("Soma: " + soma.executar(10, 5));

        /*------------------------------------------------------------------------------------------------*/
        //📌 2. Function<T, R>
        System.out.println("\nFunction:");

        Function<String, Integer> tamanho =
                texto -> texto.length();

        System.out.println("Tamanho: " + tamanho.apply("Java"));

        /*------------------------------------------------------------------------------------------------*/
        //📌 3. Consumer<T>
        System.out.println("\nConsumer:");

        Consumer<String> imprimir =
                texto -> System.out.println("Texto: " + texto);

        imprimir.accept("Hello");

        /*------------------------------------------------------------------------------------------------*/
        //📌 4. Supplier<T>
        System.out.println("\nSupplier:");

        Supplier<Double> gerarNumero =
                () -> Math.random();

        System.out.println("Número gerado: " + gerarNumero.get());

        /*------------------------------------------------------------------------------------------------*/
        //📌 5. Optional - criando
        System.out.println("\nOptional:");

        Optional<String> nome = Optional.of("Maria");
        System.out.println(nome.get());

        /*------------------------------------------------------------------------------------------------*/
        //📌 6. Optional vazio
        Optional<String> vazio = Optional.empty();

        System.out.println("Está presente? " + vazio.isPresent());

        /*------------------------------------------------------------------------------------------------*/
        //📌 7. Evitando NullPointerException
        Optional<String> possivelNome = Optional.ofNullable(null);

        System.out.println("Nome ou padrão: " + possivelNome.orElse("Desconhecido"));

        /*------------------------------------------------------------------------------------------------*/
        //📌 8. ifPresent
        Optional<String> nome1 = Optional.of("João");

        nome1.ifPresent(n ->
                System.out.println("Nome encontrado: " + n)
        );

        /*------------------------------------------------------------------------------------------------*/
        //📌 9. map com Optional
        Optional<String> nome2 = Optional.of("Ana");

        Optional<Integer> tamanhoNome =
                nome2.map(n -> n.length());

        tamanhoNome.ifPresent(t ->
                System.out.println("Tamanho do nome: " + t)
        );

        /*------------------------------------------------------------------------------------------------*/
        //📌 10. orElse vs orElseGet
        Optional<String> nome3 = Optional.empty();

        System.out.println("orElse: " + nome3.orElse("Valor padrão"));

        System.out.println("orElseGet: " +
                           nome3.orElseGet(() -> "Gerado dinamicamente"));

    }

    /*------------------------------------------------------------------------------------------------*/
    // Interface funcional customizada
    @FunctionalInterface
    interface Operacao {
        int executar(int a, int b);
    }
}
