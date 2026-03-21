package com.ada.vendas;

import java.util.concurrent.CompletableFuture;

public class ReviewAsync {

    public static void main(String[] args) throws InterruptedException {

        /*------------------------------------------------------------------------------------------------*/
        //📌 1. Execução síncrona
        System.out.println("Execução síncrona:");

        tarefaLenta();

        System.out.println("Main terminou (síncrono)");

        /*------------------------------------------------------------------------------------------------*/
        //📌 2. Execução assíncrona (runAsync)
        System.out.println("\nExecução assíncrona:");

        CompletableFuture.runAsync(() -> tarefaLenta());

        System.out.println("Main terminou (assíncrono)");

        Thread.sleep(3000); // apenas para visualizar

        /*------------------------------------------------------------------------------------------------*/
        //📌 3. Retornando valor (supplyAsync)
        System.out.println("\nRetorno com CompletableFuture:");

        CompletableFuture<Integer> futuro =
                CompletableFuture.supplyAsync(() -> calcular());

        futuro.thenAccept(resultado ->
                System.out.println("Resultado: " + resultado)
        );

        Thread.sleep(2000);

        /*------------------------------------------------------------------------------------------------*/
        //📌 4. Encadeando tarefas
        System.out.println("\nEncadeamento:");

        CompletableFuture
                .supplyAsync(() -> 10)
                .thenApply(valor -> valor * 2)
                .thenAccept(resultado ->
                        System.out.println("Resultado final: " + resultado)
                );

        Thread.sleep(2000);

        /*------------------------------------------------------------------------------------------------*/
        //📌 5. thenRun vs thenApply vs thenAccept
        System.out.println("\nDiferenças:");

        CompletableFuture
                .supplyAsync(() -> 5)
                .thenApply(v -> v * 2)       // transforma
                .thenAccept(v -> System.out.println("Valor: " + v)); // consome

        Thread.sleep(2000);

        /*------------------------------------------------------------------------------------------------*/
        //📌 6. allOf (múltiplas tarefas)
        System.out.println("\nExecutando várias tarefas:");

        CompletableFuture<Void> t1 =
                CompletableFuture.runAsync(() -> tarefa("Tarefa 1"));

        CompletableFuture<Void> t2 =
                CompletableFuture.runAsync(() -> tarefa("Tarefa 2"));

        CompletableFuture<Void> t3 =
                CompletableFuture.runAsync(() -> tarefa("Tarefa 3"));

        CompletableFuture.allOf(t1, t2, t3).join();

        System.out.println("Todas terminaram");
    }

    /*------------------------------------------------------------------------------------------------*/
    private static void tarefaLenta() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Tarefa finalizada - " + Thread.currentThread().getName());
    }

    /*------------------------------------------------------------------------------------------------*/
    private static int calcular() {
        return 42;
    }

    /*------------------------------------------------------------------------------------------------*/
    private static void tarefa(String nome) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println(nome + " - " + Thread.currentThread().getName());
    }

    /*

        → thenApply → transforma
        → thenAccept → consome

        → thenApply → entra e sai valor
        → thenAccept → entra valor, não retorna
        → thenRun → não entra nem sai valor

        ---------------------------------------

        →→→→→→ Paralelismo → executar mais rápido
        →→→→→→ Assincronismo → não bloquear o fluxo

        → CompletableFuture usa threads
        → MAS o objetivo é diferente

        →→→→→→ “Assincronismo não é sobre velocidade, é sobre não delegar e se quiser esperar.”

        Aula 1 → Datas
        Aula 2 → Funcional
        Aula 3 → Interfaces + Optional
        Aula 4 → Streams
        Aula 5 → IO
        Aula 6 → Paralelismo
        Aula 7 → Assíncrono

     */
}
