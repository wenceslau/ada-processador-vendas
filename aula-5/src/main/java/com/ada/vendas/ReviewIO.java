package com.ada.vendas;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewIO {

    public static void main(String[] args) throws IOException {

        String caminho = "files/exemplo.txt";

        /*------------------------------------------------------------------------------------------------*/
        //📌 1. Criando Path
        System.out.println("Criando Path:");

        Path path = Paths.get(caminho);
        System.out.println(path);

        /*------------------------------------------------------------------------------------------------*/
        //📌 2. Escrevendo arquivo
        System.out.println("\nEscrevendo arquivo:");

        List<String> linhas = List.of(
                "Maria,Notebook,3500",
                "Joao,Mouse,120",
                "Ana,Cadeira,800"
        );

        Files.write(path, linhas);

        /*------------------------------------------------------------------------------------------------*/
        //📌 3. Lendo arquivo
        System.out.println("\nLendo arquivo:");

        List<String> conteudo = Files.readAllLines(path);
        conteudo.forEach(System.out::println);

        /*------------------------------------------------------------------------------------------------*/
        //📌 4. Lendo com Stream
        System.out.println("\nLendo com Stream:");

        Files.lines(path)
                .forEach(System.out::println);

        /*------------------------------------------------------------------------------------------------*/
        //📌 5. Processando arquivo com Stream
        System.out.println("\nProcessando valores > 1000:");

        List<String> filtrado =
                Files.lines(path)
                        .filter(linha -> {
                            String[] dados = linha.split(",");
                            double valor = Double.parseDouble(dados[2]);
                            return valor > 1000;
                        })
                        .collect(Collectors.toList());

        filtrado.forEach(System.out::println);

        /*------------------------------------------------------------------------------------------------*/
        //📌 6. Transformando dados (map)
        System.out.println("\nExtraindo nomes:");

        List<String> nomes =
                Files.lines(path)
                        .map(linha -> linha.split(",")[0])
                        .collect(Collectors.toList());

        nomes.forEach(System.out::println);

        /*------------------------------------------------------------------------------------------------*/
        //📌 7. Criando novo arquivo (relatório)
        System.out.println("\nGerando relatório:");

        List<String> relatorio =
                Files.lines(path)
                        .map(linha -> linha.split(","))
                        .map(dados -> "Cliente: " + dados[0] + " - Valor: " + dados[2])
                        .collect(Collectors.toList());

        Path relatorioPath = Paths.get("files/relatorio.txt");
        Files.write(relatorioPath, relatorio);

        System.out.println("Relatório gerado!");

        /*------------------------------------------------------------------------------------------------*/
        //📌 8. Verificando se arquivo existe
        System.out.println("\nArquivo existe?");

        boolean existe = Files.exists(path);
        System.out.println(existe);
    }
}
