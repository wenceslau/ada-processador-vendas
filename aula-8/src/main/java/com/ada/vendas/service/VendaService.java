package com.ada.vendas.service;

import com.ada.vendas.io.VendaFileReader;
import com.ada.vendas.model.Venda;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class VendaService {

    public List<Venda> importarVenda(String arquivo) {

        System.out.printf("%s: Importacao dados de vendas\n", LocalTime.now());

        try {
             var vendas = VendaFileReader.carregarVendas(arquivo);

            System.out.printf("%s: Importacao dados de vendas concluida\n", LocalTime.now());

            return vendas;

        } catch (IOException e) {
            throw new RuntimeException("Ocorreu um problema ao importar as vendas. Causa:" + e.getMessage());
        }
    }

    public Map<Integer, Double> vendasPorMes(List<Venda> vendas, int ano) {

        if (vendas == null){
            throw new RuntimeException("Nehuma venda foi importada");
        }

        if (ano < 0){
            throw new RuntimeException("Ano invalido");
        }
        System.out.printf("%s: Extração dados de vendas por mes \n", LocalTime.now());

        var totalVendaPorMes = vendas.parallelStream()
                .filter(v -> v.getDataVenda().getYear() == ano)
                .collect(Collectors.groupingBy(v -> v.getDataVenda().getMonthValue(),
                        Collectors.summingDouble(Venda::getValorTotal)
                ));

        System.out.printf("%s: Extração finalilizada. \n", LocalTime.now());
        return totalVendaPorMes;
    }

    public Map<String, Double> vendasPorCliente(List<Venda> vendas) {
        if (vendas == null){
            throw new RuntimeException("Nehuma venda foi importada");
        }

        System.out.printf("%s: Extração dados de vendas por cliente \n", LocalTime.now());

        var totalVendaPorCliente = vendas.parallelStream()
                .collect(Collectors.groupingBy(v -> v.getCliente().getNome(),
                        Collectors.summingDouble(Venda::getValorTotal)
                ));

        System.out.printf("%s: Extração finalilizada. \n", LocalTime.now());

        return totalVendaPorCliente;
    }

    public void cashbackEmail(List<Venda> vendas) {
        if (vendas == null){
            throw new RuntimeException("Nehuma venda foi importada");
        }
        CompletableFuture.supplyAsync(() -> vendas)
                .thenRunAsync(() -> vendas.forEach(this::calcularCashback))
                .thenApply(total -> vendas.parallelStream().mapToInt(this::enviarEmailCashback).sum())
                .thenAccept(totalEmails -> System.out.println("Emails cashback com sucesso. Total emails enviado: " + totalEmails));

        System.out.printf("%s: O calculo de cashback e envio do email esta em curso, e você será notificado assim que finalizar. \n", LocalTime.now());
    }

    private void calcularCashback(Venda venda) {
        if (venda.getValorTotal() <= 1000) {
            venda.setCashback(venda.getValorTotal() * 0.01);
        } else {
            venda.setCashback(venda.getValorTotal() * 0.02);
        }
    }

    private int enviarEmailCashback(Venda venda) {
        try {
            if (venda.getCliente().getEmail() == null || venda.getCliente().getEmail().isEmpty()){
                throw new Exception("email invalido");
            }
            //Simular o envio do email, com um delay de 300 ms para cada envio
            Thread.sleep(300);
            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

}
