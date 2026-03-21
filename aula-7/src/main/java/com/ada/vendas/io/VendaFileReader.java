package com.ada.vendas.io;

import com.ada.vendas.model.Cliente;
import com.ada.vendas.model.Produto;
import com.ada.vendas.model.Venda;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class VendaFileReader {

    public static List<String> lerLinhas(String caminho) throws IOException {

        Path path = Paths.get(caminho);

        return Files.readAllLines(path);

        /*
            Path → representa caminho
            Files → utilitário para manipular arquivos
         */
    }

    public static Venda converterLinha(String linha) {

        String[] dados = linha.split(",");

        Long id = Long.parseLong(dados[0]);
        String nomeCliente = dados[1];
        String nomeProduto = dados[2];
        String categoria = dados[3];
        Integer quantidade = Integer.parseInt(dados[4]);
        Double valor = Double.parseDouble(dados[5]);
        LocalDate data = LocalDate.parse(dados[6]);

        Cliente cliente = new Cliente(null, nomeCliente, "");
        Produto produto = new Produto(null, nomeProduto, categoria);

        return new Venda(id, cliente, produto, quantidade, valor, data);
    }

    public static List<Venda> carregarVendas(String caminho) throws IOException {

        return Files.lines(Paths.get(caminho))               // ler arquivo, retorna stream de linhas
                .map(linha -> converterLinha(linha))   // processar cada linha, criar objeto Venda
                .toList();                                   // converter stream para lista
    }

}
