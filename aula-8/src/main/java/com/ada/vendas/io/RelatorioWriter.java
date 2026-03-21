package com.ada.vendas.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class RelatorioWriter {

    public static File escreverRelatorio(String caminho, List<String> linhas) {

        try {
            Path path = Paths.get(caminho);
            Files.write(path, linhas);

            return path.toFile();

        } catch (IOException e) {
            throw  new RuntimeException("Ocorreu um erro ao escrever o relatorio.", e);
        }
    }

}
