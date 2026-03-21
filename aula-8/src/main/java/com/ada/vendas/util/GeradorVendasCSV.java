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
            "Maria", "Joao", "Ana", "Carlos", "Julia", "Marcos", "Pedro", "Lucas", "Fernanda", "Bruno"
    );

    private static final List<String> PROVEDORES = List.of(
            "gmail", "hotmail", "yahoo", "outlook"
    );

    private static final List<String> PRODUTOS_ELETRONICOS = List.of(
            "Notebook", "Mouse", "Teclado", "Monitor", "Headset", "TV"
    );

    private static final List<String> PRODUTOS_MOVEIS = List.of(
            "Mesa", "Cadeira", "Cama", "Sofá", "Armário"
    );


    private static final List<String> CATEGORIAS = List.of(
            "Eletronicos", "Moveis"
    );

    public static void gerarArquivo(String caminho, int quantidade) throws IOException {

        Random random = new Random();

        List<String> linhas = IntStream.rangeClosed(1, quantidade)
                .mapToObj(id -> {

                    String cliente = CLIENTES.get(random.nextInt(CLIENTES.size()));
                    String email = cliente + "@" + PROVEDORES.get(random.nextInt(PROVEDORES.size()));
                    String categoria = CATEGORIAS.get(random.nextInt(CATEGORIAS.size()));
                    String produto;
                    if (categoria.equals("Eletronicos")) {
                        produto = PRODUTOS_ELETRONICOS.get(random.nextInt(PRODUTOS_ELETRONICOS.size()));
                    } else {
                        produto = PRODUTOS_MOVEIS.get(random.nextInt(PRODUTOS_MOVEIS.size()));
                    }

                    if (cliente.equals("Ana")) {
                        email = "";
                    }

                    int quantidadeVenda = random.nextInt(3) + 1;
                    double valor = 100 + random.nextInt(3500);

                    LocalDate data = LocalDate.of(2026,
                            random.nextInt(12) + 1,
                            random.nextInt(25) + 1);

                    return id + "," +
                           cliente + "," +
                           email.toLowerCase() + "," +
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
