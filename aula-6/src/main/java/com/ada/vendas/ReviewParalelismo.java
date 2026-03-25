package com.ada.vendas;

import com.ada.vendas.model.Venda;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ReviewParalelismo {

    public static void main(String[] args) throws InterruptedException {

        List<Venda> vendas = gerarVendasFake();

        /*------------------------------------------------------------------------------------------------*/
        //📌 1. Processamento sequencial
        System.out.println("Processamento sequencial:");

        long inicio = System.currentTimeMillis();

        vendas.stream()
                .forEach(v -> processar(v));

        long fim = System.currentTimeMillis();

        System.out.println("Tempo sequencial: " + (fim - inicio) + " ms");

        /*------------------------------------------------------------------------------------------------*/
        //📌 2. Processamento paralelo (parallelStream)
        System.out.println("\nProcessamento paralelo:");

        long inicioParalelo = System.currentTimeMillis();

        vendas.parallelStream()
                .forEach(v -> processar(v));

        long fimParalelo = System.currentTimeMillis();

        System.out.println("Tempo paralelo: " + (fimParalelo - inicioParalelo) + " ms");

        /*------------------------------------------------------------------------------------------------*/
        //📌 3. Criando Thread manualmente
        System.out.println("\nThread manual:");

        Thread thread = new Thread(() ->
                System.out.println("Executando em: " + Thread.currentThread().getName())
        );
        thread.start();

        /*------------------------------------------------------------------------------------------------*/
        //📌 4. ExecutorService
        System.out.println("\nExecutorService:");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (Venda venda : vendas) {
            executor.submit(() -> processar(venda));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        /*------------------------------------------------------------------------------------------------*/
        //📌 5. Mostrando threads
        System.out.println("\nThreads em execução:");

        vendas.parallelStream()
                .forEach(v -> {
                    System.out.println(Thread.currentThread().getName());
                    processar(v);
                });
    }

    /*------------------------------------------------------------------------------------------------*/
    private static void processar(Venda venda) {

        try {
            Thread.sleep(200); // simula processamento pesado
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Processando venda " + venda.getId());
    }

    /*------------------------------------------------------------------------------------------------*/
    private static List<Venda> gerarVendasFake() {

        List<Venda> vendas = new ArrayList<>();

        vendas.add(new Venda(1L, null, null, 1, 1000.0, null));
        vendas.add(new Venda(2L, null, null, 1, 2000.0, null));
        vendas.add(new Venda(3L, null, null, 1, 3000.0, null));
        vendas.add(new Venda(4L, null, null, 1, 4000.0, null));

        return vendas;
    }

    /*
        -> Paralelismo divide o trabalho para terminar mais rápido
        -> Nem sempre paralelismo é mais rápido
     */
}
