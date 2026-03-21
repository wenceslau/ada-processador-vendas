package com.ada.vendas.io;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class RelatorioWriter {

    public static void escreverRelatorio(String caminho, List<String> linhas) throws IOException {

        Path path = Paths.get(caminho);

        Files.write(path, linhas);
    }

}
