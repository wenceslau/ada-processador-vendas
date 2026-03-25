package com.ada.vendas.service;

import com.ada.vendas.io.RelatorioWriter;
import com.ada.vendas.model.Venda;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioService {

    public File relatorioVendaPorCliente(List<Venda> vendas) {

        if (vendas == null){
            throw new RuntimeException("Nehuma venda foi importada");
        }

        var vendasPorCliente = vendas.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getCliente().getNome()
                ));

        var relatorio = new ArrayList<String>();
        vendasPorCliente.forEach((cliente, lista) -> {

            relatorio.add("---------------------------------------------");
            relatorio.add("Cliente: " + cliente);
            lista.forEach(v -> relatorio.add("  " + v.toString()));
            relatorio.add("");

        });

        var dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        var nomeRelatorio = "files/relatorio_vendas_por_cliente_" + dataHora + ".txt";

        return RelatorioWriter.escreverRelatorio(nomeRelatorio, relatorio);

    }

    public File relatorioVendaPorMes(List<Venda> vendas, int ano){
        if (vendas == null){
            throw new RuntimeException("Nehuma venda foi importada");
        }
        var vendasPorMes = vendas.stream()
                .filter(v -> v.getDataVenda().getYear() == ano)
                .collect(Collectors.groupingBy(
                        v -> v.getDataVenda().getMonthValue()
                ));

        var relatorio = new ArrayList<String>();
        vendasPorMes.forEach((mes, lista) -> {

            relatorio.add("---------------------------------------------");
            relatorio.add("Mês: " + mes);
            lista.forEach(v -> relatorio.add("  " + v.toString()));
            relatorio.add("");

        });

        var dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        var nomeRelatorio = "files/relatorio_vendas_por_mes_" + dataHora + ".txt";

        return RelatorioWriter.escreverRelatorio(nomeRelatorio, relatorio);
    }

    public File relatorioVendaPorCategoria(List<Venda> vendas){
        if (vendas == null){
            throw new RuntimeException("Nehuma venda foi importada");
        }

        var vendasPorCategoria = vendas.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getProduto().getCategoria()
                ));

        var relatorio = new ArrayList<String>();
        vendasPorCategoria.forEach((categoria, lista) -> {

            relatorio.add("---------------------------------------------");
            relatorio.add("Categoria: " + categoria);
            lista.forEach(v -> relatorio.add("  " + v.toString()));
            relatorio.add("");

        });

        var dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        var nomeRelatorio = "files/relatorio_vendas_por_categoria_" + dataHora + ".txt";

        return RelatorioWriter.escreverRelatorio(nomeRelatorio, relatorio);

    }

}
