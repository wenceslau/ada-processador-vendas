package com.ada.vendas.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeradorVendasCSV {

    private static final List<String> CLIENTES = List.of(
            "Maria","Joao","Ana","Carlos","Julia","Marcos","Pedro","Lucas","Fernanda","Bruno"
    );

    private static final List<String> PRODUTOS = List.of(
            "Notebook","Mouse","Teclado","Monitor","Headset","Mesa","Cadeira"
    );

    private static final List<String> CATEGORIAS = List.of(
            "Eletronicos","Moveis"
    );

    public static void gerarArquivo(String caminho, int quantidade) throws IOException {

        Random random = new Random();

        List<String> linhas = IntStream.rangeClosed(1, quantidade)
                .mapToObj(id -> {

                    String cliente = CLIENTES.get(random.nextInt(CLIENTES.size()));
                    String produto = PRODUTOS.get(random.nextInt(PRODUTOS.size()));
                    String categoria = CATEGORIAS.get(random.nextInt(CATEGORIAS.size()));

                    int quantidadeVenda = random.nextInt(3) + 1;
                    double valor = 100 + random.nextInt(3500);

                    LocalDate data = LocalDate.of(2026,
                            random.nextInt(12) + 1,
                            random.nextInt(25) + 1);

                    return id + "," +
                           cliente + "," +
                           produto + "," +
                           categoria + "," +
                           quantidadeVenda + "," +
                           valor + "," +
                           data;
                })
                .collect(Collectors.toList());

        Files.write(Paths.get(caminho), linhas);
    }

}
