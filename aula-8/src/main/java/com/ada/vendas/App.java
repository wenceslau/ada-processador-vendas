package com.ada.vendas;

import com.ada.vendas.model.Venda;
import com.ada.vendas.service.RelatorioService;
import com.ada.vendas.service.VendaService;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {

    private static List<Venda> vendas;

    public static void main(String[] args) {

        String arquivoVendas = caminhoArquivoVendas();
        Scanner scanner = new Scanner(System.in);
        VendaService vendaService = new VendaService();
        RelatorioService relatorioService = new RelatorioService();


        while (true) {
            System.out.println("_______________________________________________");
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Importar vendas");
            System.out.println("2 - Total de Vendas por mês");
            System.out.println("3 - Total de Vendas por cliente");
            System.out.println("4 - Calcular cashback e enviar notificação (assincrono)");
            System.out.println("5 - Gerar relatório vendas por Cliente");
            System.out.println("6 - Gerar relatório vendas por Mês");
            System.out.println("7 - Gerar relatório por Categoria");
            System.out.println("0 - Sair");

            try {
                int opcao = scanner.nextInt();
                switch (opcao) {
                    case 1 -> vendas = vendaService.importarVenda(arquivoVendas);
                    case 2 -> vendasPorMes(vendaService);
                    case 3 -> vendasPorCliente(vendaService);
                    case 4 -> vendaService.cashbackEmail(vendas);
                    case 5 -> gerarRelatorio(relatorioService, 1);
                    case 6 -> gerarRelatorio(relatorioService, 2);
                    case 7 -> gerarRelatorio(relatorioService, 3);
                    case 0 -> System.exit(0);
                    default -> System.out.println("Opção inválida");
                }
            } catch (Exception ex) {
                System.out.println("Algo deu errado. Tente novamente. [" + ex.getMessage() + "]");
            }
        }

    }

    private static String caminhoArquivoVendas() {
        String arquivoVendas = "files/vendas_aula_8.csv";
        return arquivoVendas;
    }

    private static void vendasPorMes(VendaService vendaService) {
        int ano = 2026;
        Map<Integer, Double>
                dados = vendaService.vendasPorMes(vendas, ano);


        System.out.println("Total de vendas ano " + ano + " por mês");
        System.out.println("| Mês   Total\t|");
        dados.forEach((mes, total) ->
                System.out.println("| " + mes + "   " + total + "   |")
        );

    }

    private static void vendasPorCliente(VendaService vendaService) {

        Map<String, Double> dados = vendaService.vendasPorCliente(vendas);
        System.out.println("Total de vendas por cliente");

        System.out.println("| Cliente   Total|");
        dados.forEach((cliente, total) ->
                System.out.println("| " + cliente + "   " + total + "|")
        );

    }

    private static void gerarRelatorio(RelatorioService relatorioService, int tipo) {

        File relatorio = null;
        if (tipo == 1) {
            relatorio = relatorioService.relatorioVendaPorCliente(vendas);
        } else if (tipo == 2) {
            int ano = 2026;
            relatorio = relatorioService.relatorioVendaPorMes(vendas, ano);
        } else if (tipo == 3) {
            relatorio = relatorioService.relatorioVendaPorCategoria(vendas);
        }

        if (relatorio != null) {
            System.out.println("Relatorio gerado com sucesso: " + relatorio.getAbsolutePath());
        }
    }
}
