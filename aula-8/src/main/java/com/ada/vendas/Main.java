package com.ada.vendas;

import com.ada.vendas.io.RelatorioWriter;
import com.ada.vendas.io.VendaFileReader;
import com.ada.vendas.model.Venda;
import com.ada.vendas.util.GeradorVendasCSV;

import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {


        GeradorVendasCSV.gerarArquivo("files/vendas_grandes.csv", 5);
        List<Venda> vendas = VendaFileReader.carregarVendas("files/vendas_grandes.csv");

        // 1 - Percorre as vendas e calcula o bonus para cada venda, e depois envia o email com o cashback
        vendas.forEach(venda ->
                CompletableFuture
                        .runAsync(() -> calcularCashback(venda))
                        .thenRunAsync(() -> enviarEmailCashback(venda))
        );
        System.out.println("Programa continua a executar");
        Thread.sleep(100000);

        // 2 - Cria 2 tarefa encadeada, calcular cashback e enviar email, um apos o outro
        CompletableFuture<Void> future = CompletableFuture
                .runAsync(() -> calcularCashback(vendas))
                .thenRunAsync(() -> enviarEmailCashback(vendas));

        System.out.println("Programa continua a executar");
        System.out.println("Executnando outras tarefas");
        System.out.println("Tarefa 1");
        System.out.println("Tarefa 2");
        System.out.println("Tarefa 3");
        future.join();

        // 3 - cria execução async de tarefas encadeada com retorno da ultima tarefa
        CompletableFuture<Void> future2 = CompletableFuture.supplyAsync(() -> vendas)
                .thenRunAsync(() -> calcularCashback(vendas))
                .thenRun(() -> System.out.println("Bonus calculado"))
                .thenApply(total -> enviarEmailCashback(vendas))
                .thenAccept(totalEmails -> System.out.println("Total emails enviado: " + totalEmails));

        System.out.println("Programa continua a executar");
        System.out.println("Programa continua a executar");
        System.out.println("Executnando outras tarefas");
        System.out.println("Tarefa 1");
        System.out.println("Tarefa 2");
        System.out.println("Tarefa 3");
        future2.join();

        CompletableFuture<Void> tarefa1 =
                CompletableFuture.runAsync(() -> {
                    System.out.println("Processando tarefa 1");
                });
        CompletableFuture<Void> tarefa2 =
                CompletableFuture.runAsync(() -> {
                    System.out.println("Processando tarefa 2");
                });
        CompletableFuture<Void> tarefa3 =
                CompletableFuture.runAsync(() -> {
                    System.out.println("Processando tarefa 3");
                });

        CompletableFuture<Void> todas =
                CompletableFuture.allOf(tarefa1, tarefa2, tarefa3);

        todas.join();

        // Exercicio

        CompletableFuture<Void> tarefa11 =
                CompletableFuture.runAsync(() -> {
                    List<Venda> vendasCaras =
                            vendas.stream()
                                    .filter(v -> v.getValorTotal() > 1000)
                                    .toList();

                    List<String> relatorio2 = new ArrayList<>();
                    relatorio2.add("Vendas acima de 1000");

                    vendasCaras.forEach(v ->
                            relatorio2.add(v.toString()));

                    RelatorioWriter.escreverRelatorio(
                            "relatorio_vendas_acima_1000.txt",
                            relatorio2
                    );
                });
        CompletableFuture<Void> tarefa22 =
                CompletableFuture.runAsync(() -> {
                    Map<Month, Double> totalPorMes =
                            vendas.stream()
                                    .collect(Collectors.groupingBy(
                                            v -> v.getDataVenda().getMonth(),
                                            Collectors.summingDouble(Venda::getValorTotal)
                                    ));

                    List<String> relatorio3 = new ArrayList<>();
                    relatorio3.add("Total vendido por mês");
                    totalPorMes.forEach((mes, total2) ->
                            relatorio3.add(mes + " -> " + total2));

                    RelatorioWriter.escreverRelatorio(
                            "relatorio_total_por_mes.txt",
                            relatorio3
                    );
                });
        CompletableFuture<Void> tarefa33 =
                CompletableFuture.runAsync(() -> {
                    Map<String, List<Venda>> vendasPorCliente =
                            vendas.stream()
                                    .collect(Collectors.groupingBy(
                                            v -> v.getCliente().getNome()
                                    ));

                    List<String> relatorio4 = new ArrayList<>();
                    vendasPorCliente.forEach((cliente, lista) -> {

                        relatorio4.add("Cliente: " + cliente);

                        lista.forEach(v ->
                                relatorio4.add("  " + v.toString()));

                        relatorio4.add("");
                    });
                    RelatorioWriter.escreverRelatorio(
                            "relatorio_vendas_por_cliente.txt",
                            relatorio4
                    );
                });

        CompletableFuture<Void> todas1 = CompletableFuture.allOf(tarefa11, tarefa22, tarefa33);

        todas.join();

    }

    public static void calcularCashback(List<Venda> venda) {
        System.out.println("Trace. Calculando bonus. " + Thread.currentThread().getName());
        venda.forEach(v -> calcularCashback(v));
    }

    public static int enviarEmailCashback(List<Venda> venda) {
        System.out.println("Trace. Enviando email. " + Thread.currentThread().getName());
        int totalEmails = venda.stream()
                .mapToInt(v -> enviarEmailCashback(v))
                .sum();
        return totalEmails;
    }

    public static void calcularCashback(Venda venda) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(LocalTime.now() + ". Calculando bonus venda ID " + venda.getId());
        // venda até mill reais, cashback de 1 % sobre a venda
        if (venda.getValorTotal() <= 1000) {
            venda.setCashback(venda.getValorTotal() * 0.01);
        } else {
            venda.setCashback(venda.getValorTotal() * 0.02);
        }
    }

    public static int enviarEmailCashback(Venda venda) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        try {
            System.out.println(LocalTime.now() + ". Enviando email venda ID " + venda.getId() + " para o cliente " + venda.getCliente().getNome() + " com cashback de " + venda.getCashback());
            return 1;
        } catch (Exception ex) {
            System.out.println("Erro ao enviar email");
            return 0;
        }
    }

    /*

        Predicate	Testa condição - boolean
        Consumer	Executa ação - sem retorno
        Function	Transforma valor - recebe valor e retorna valor
        Optional    Valor pode existir ou não

     */

    /*

    ✅ Aula 1 → Datas
    ✅ Aula 2 → Predicate / comportamento como parâmetro
    ✅ Aula 3 → Function / Consumer / Optional
    ✅ Aula 4 → Streams (filter, map, collect)

        loop → predicate → stream

        Step 1 – Imperative
        for (Venda venda : vendas) {
            if (venda.getValorTotal() > 1000) {
                resultado.add(venda);
            }
        }

        Step 2 – Functional with Predicate
        VendaUtils.filtrarVendas(vendas,
                v -> v.getValorTotal() > 1000);

        Step 3 – Streams
        vendas.stream()
              .filter(v -> v.getValorTotal() > 1000)
              .toList();

     */

    /*
        TODO Exercicios
            Exercício 1 — Filtrar vendas acima de 1000, e gerar um relatorio com o resultado
            Exercício 2 — Calcular valor total vendido por mes, e gerar um relatorio com o resultado
            Exercício 3 — Agrupar vendas por cliente (Desafio), e gerar um relatorio com o resultado
            Exercício bônus - Crie um relatório contendo o total vendido por cliente.

     */

    /*
        “Stream não é uma estrutura de dados.
        É uma forma de processar dados.”
     */

    /*
       TODO Exercício 1 — Executar múltiplos relatórios em paralelo
            Crie tarefas assíncronas para gerar relatorios:
            1️⃣ vendasCaras
            2️⃣ totalPorMes
            3️⃣ vendasPorCliente
            Utilize CompletableFuture.allOf() para esperar todas as tarefas terminarem.
            Fluxo:
            relatorio1
            relatorio2
            relatorio3
                ↓
            allOf()
                ↓
            mostrar resultados
            Objetivo:
            executar múltiplas tarefas
            coordenar tarefas com allOf

     */

    public static void exercicios(List<Venda> vendas) {

    }
}
