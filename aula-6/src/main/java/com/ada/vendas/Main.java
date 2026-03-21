package com.ada.vendas;

import com.ada.vendas.io.RelatorioWriter;
import com.ada.vendas.io.VendaFileReader;
import com.ada.vendas.model.Cliente;
import com.ada.vendas.model.Produto;
import com.ada.vendas.model.Venda;
import com.ada.vendas.util.GeradorVendasCSV;
import com.ada.vendas.viacep.ViaCep;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        //processarVendasDeMemoria();
        //processarVendasDeArquivo();

        //streamSequencial(vendas);
        //streamParalelo(vendas);
       // executorService(vendas);

        GeradorVendasCSV.gerarArquivo("files/vendas_aula_8.csv", 1000);
        //List<Venda> vendas = VendaFileReader.carregarVendas("vendas_grandes.csv");
        //exercicios(vendas);


        //consultaCep();

//        List<Venda> vendas = gerarVendasExemplo();
//

//        long inicio = System.currentTimeMillis();
//        double total =
//                vendas.stream()
//                        .mapToDouble(Venda::getValorTotal)
//                        .sum();
//        long fim = System.currentTimeMillis();
//        System.out.println("Tempo de processamento 1: " + (fim - inicio) + "ms. " + total);
//
//        inicio = System.currentTimeMillis();
//        total =
//                vendas.parallelStream()
//                        .mapToDouble(Venda::getValorTotal)
//                        .sum();
//        fim = System.currentTimeMillis();
//        System.out.println("Tempo de processamento 2: " + (fim - inicio) + "ms. " + total);
//
//        inicio = System.currentTimeMillis();
//        int quantidade =
//                vendas.stream()
//                        .mapToInt(Venda::getQuantidade)
//                        .sum();
//        fim = System.currentTimeMillis();
//        System.out.println("Tempo de processamento 3: " + (fim - inicio) + "ms. " + quantidade);
//
//        inicio = System.currentTimeMillis();
//        quantidade =
//                vendas.parallelStream()
//                        .mapToInt(Venda::getQuantidade)
//                        .sum();
//        fim = System.currentTimeMillis();
//        System.out.println("Tempo de processamento 4: " + (fim - inicio) + "ms. " + quantidade);

    }

    private static void consultaCep() throws IOException {
        List<String> listaCep = Files.readAllLines(Paths.get("files/cep.txt"));
        long inicio = System.currentTimeMillis();
        listaCep.parallelStream()
                .map(ViaCep::consultarCep)
                .filter(json -> json != null)
                .map(ViaCep::converterToEndereco)
                .filter(endereco ->  endereco != null)
                .forEach(System.out::println);
        long fim = System.currentTimeMillis();
        System.out.println("Tempo de processamento: " + (fim - inicio) + "ms");
    }

    private static void streamSequencial(List<Venda> vendas) {
        long inicio = System.currentTimeMillis();
        vendas.stream().forEach(Main::calcularBonus);
        long fim = System.currentTimeMillis();
        System.out.println("Tempo de processamento 1: " + (fim - inicio) + "ms");
    }

    private static void streamParalelo(List<Venda> vendas) {
        //Java divide o trabalho entre os núcleos da CPU usando o ForkJoinPool.
        long inicio = System.currentTimeMillis();
        vendas.parallelStream().forEach(Main::calcularBonus);
        long fim = System.currentTimeMillis();
        System.out.println("Tempo de processamento 2: " + (fim - inicio) + "ms");
    }

    private static void executorService(List<Venda> vendas) throws InterruptedException {
        ExecutorService executor =
                Executors.newFixedThreadPool(4);
        long inicio = System.currentTimeMillis();
        for (Venda venda : vendas) {
            executor.submit(() -> calcularBonus(venda));
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        long fim = System.currentTimeMillis();
        System.out.println("Tempo de processamento 3: " + (fim - inicio) + "ms");
    }

    private static void processarVendasDeMemoria() {
        List<Venda> vendas = gerarVendasExemplo();

        // 1️⃣ Filter – Filtrar vendas por valor
        List<Venda> vendasCaras =
                vendas.stream()                                             // stream → cria fluxo
                        .filter(v -> v.getValorTotal() > 1000)       // filter → aplica Predicate
                        .toList();                                          // toList → coleta resultado

        // 2️⃣ Filter – Vendas dos últimos 7 dias
        List<Venda> vendasRecentes =
                vendas.stream()
                        .filter(v -> v.getDataVenda()
                                .isAfter(LocalDate.now().minusDays(7)))
                        .toList();

        // 3️⃣ Map – Obter valores das vendas
        List<Double> valores =
                vendas.stream()
                        .map(v -> v.getValorTotal())
                        //.map(Venda::getValorTotal)
                        .toList();

        // 4️⃣ Map – Obter nomes dos clientes
        List<String> clientes =
                vendas.stream()
                        .map(v -> v.getCliente().getNome())
                        .toList();

        // 5️⃣ forEach – Imprimir vendas
        vendas.stream()
                .forEach(v -> System.out.println(v));
        vendas.forEach(System.out::println);

        // 6️⃣ Sorting – Ordenar por valor da venda
        List<Venda> ordenadas =
                vendas.stream()
                        //.sorted(Comparator.comparing(Venda::getValorTotal))
                        .sorted(Comparator.comparing(v -> v.getValorTotal()))
                        .toList();

        List<Venda> ordenadasDesc =
                vendas.stream()
                        .sorted(Comparator.comparing(Venda::getValorTotal).reversed())
                        .toList();

        // 7️⃣ Distinct – Clientes únicos
        List<String> clientes2 =
                vendas.stream()
                        .map(v -> v.getCliente().getNome())
                        .distinct()
                        .toList();

        // 8️⃣ Limit – Top 3 vendas
        List<Venda> top3 =
                vendas.stream()
                        .sorted(Comparator.comparing(Venda::getValorTotal).reversed())
                        .limit(3)
                        .toList();

        // 9️⃣ Counting – Quantidade de vendas maior que 1000
        long total =
                vendas.stream()
                        .filter(v -> v.getValorTotal() > 1000)
                        .count();

        // 🔟 SummingDouble – Total vendido
        double totalVendas =
                vendas.stream()
                        .mapToDouble(Venda::getValorTotal)
                        .sum();

        // 1️⃣1️⃣ Average – Média das vendas
        double media =
                vendas.stream()
                        .mapToDouble(Venda::getValorTotal)
                        .average()
                        .orElse(0);

        // 1️⃣2️⃣ Max – Maior venda
        Optional<Venda> maiorVenda =
                vendas.stream()
                        //.max(Comparator.comparing(Venda::getValorTotal));
                        .max(Comparator.comparing(v -> v.getValorTotal()));
        maiorVenda.ifPresent(System.out::println);

        // 1️⃣3️⃣ GroupingBy – Agrupar por cliente
        Map<String, List<Venda>> vendasPorCliente =
                vendas.stream()
                        .collect(
                                Collectors.groupingBy(v -> v.getCliente().getNome())
                        );

        // 1️⃣4️⃣ GroupingBy + SummingDouble
        Map<String, Double> totalPorCliente =
                vendas.stream()
                        .collect(
                                Collectors.groupingBy(
                                        v -> v.getCliente().getNome(),
                                        Collectors.summingDouble(Venda::getValorTotal)
                                )
                        );

        // 1️⃣5️⃣ GroupingBy por categoria
        Map<String, List<Venda>> vendasPorCategoria =
                vendas.stream()
                        .collect(Collectors.groupingBy(
                                v -> v.getProduto().getCategoria()));

        // 🎯 Visual Pipeline Example
        vendas.stream()
                .filter(v -> v.getValorTotal() > 500)
                .map(Venda::getCliente)
                .map(Cliente::getNome)
                .distinct()
                .sorted()
                .forEach(System.out::println);

        /*
            vendas
             ↓
            filter
             ↓
            map
             ↓
            map
             ↓
            distinct
             ↓
            sorted
             ↓
            forEach
         */
    }

    public static void processarVendasDeArquivo() throws IOException {
        List<Venda> vendas = VendaFileReader.carregarVendas("files/vendas.csv");
        vendas.forEach(System.out::println);

        double total =
                vendas.stream()
                        .mapToDouble(Venda::getValorTotal)
                        .sum();

        List<String> relatorio = List.of(
                "Total de vendas: " + vendas.size(),
                "Valor total vendido: " + total
        );

        RelatorioWriter.escreverRelatorio(
                "relatorio.txt",
                relatorio
        );

        /*
            ler arquivo CSV
            ↓
            converter para objetos
            ↓
            processar com streams
            ↓
            gerar relatório
         */

        // Execicios:

        // TODO 1
        List<Venda> vendasCaras =
                vendas.stream()
                        .filter(v -> v.getValorTotal() > 1000)
                        .toList();

        List<String> relatorio2 = new ArrayList<>();
        relatorio2.add("Vendas acima de 1000");

        vendasCaras.forEach(v ->
                relatorio2.add(v.toString()));

        RelatorioWriter.escreverRelatorio(
                "files/relatorio_vendas_acima_1000.txt",
                relatorio2
        );


        // TODO 2
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

        // TODO 3
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

        // TODO 4
        Map<String, Double> totalPorCliente =
                vendas.stream()
                        .collect(Collectors.groupingBy(
                                v -> v.getCliente().getNome(),
                                Collectors.summingDouble(Venda::getValorTotal)
                        ));
        List<String> relatorio5 = new ArrayList<>();

        relatorio5.add("Total vendido por cliente");

        totalPorCliente.forEach((cliente, total3) ->
                relatorio5.add(cliente + " -> " + total3));

        RelatorioWriter.escreverRelatorio(
                "relatorio_total_por_cliente.txt",
                relatorio5
        );

        /*
            CSV file
               ↓
            Read file (NIO2)
               ↓
            Convert to objects
               ↓
            Process with Streams
               ↓
            Generate reports
         */
    }

    public static List<Venda> gerarVendasExemplo() {

        List<Venda> vendas = new ArrayList<>();

        Cliente cliente1 = new Cliente(1L, "Maria", "maria@email.com");
        Cliente cliente2 = new Cliente(2L, "João", "joao@email.com");
        Cliente cliente3 = new Cliente(3L, "Ana", "ana@email.com");

        Produto produto1 = new Produto(1L, "Notebook", "Eletrônicos");
        Produto produto2 = new Produto(2L, "Mouse", "Eletrônicos");
        Produto produto3 = new Produto(3L, "Teclado", "Eletrônicos");
        Produto produto4 = new Produto(4L, "Cadeira", "Móveis");

        vendas.add(new Venda(1L, cliente1, produto1, 1, 3500.0, LocalDate.now().minusDays(2)));
        vendas.add(new Venda(2L, cliente2, produto2, 2, 120.0, LocalDate.now().minusDays(10)));
        vendas.add(new Venda(3L, cliente3, produto3, 1, 200.0, LocalDate.now().minusDays(30)));
        vendas.add(new Venda(4L, cliente1, produto4, 1, 800.0, LocalDate.now().minusMonths(1)));
        vendas.add(new Venda(5L, cliente2, produto1, 1, 3500.0, LocalDate.now().minusDays(5)));
        vendas.add(new Venda(6L, cliente3, produto2, 3, 120.0, LocalDate.now().minusDays(1)));
        vendas.add(new Venda(7L, cliente1, produto3, 2, 200.0, LocalDate.now().minusMonths(2)));
        vendas.add(new Venda(8L, cliente2, produto4, 1, 900.0, LocalDate.now().minusDays(15)));
        vendas.add(new Venda(9L, cliente3, produto1, 1, 3500.0, LocalDate.now().minusDays(7)));
        vendas.add(new Venda(10L, cliente1, produto2, 4, 120.0, LocalDate.now().minusDays(3)));

        return vendas;
    }

    public static void calcularBonus(Venda venda) {
        try {
            System.out.println("Calculando bonus venda ID " + venda.getId());
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
       TODO  Exercício 1 — Comparar tempo sequencial vs paralelo
                Carregue o arquivo vendas_grandes.csv.
                Calcule o a quantidade total de items vendido:
                1️⃣ stream()
                2️⃣ parallelStream()
                Meça o tempo de execução.
                Objetivo:
                observar diferença de desempenho

       TODO Exercício 2 — Processar vendas com ExecutorService
                Utilize um ExecutorService com 4 threads para processar todas as vendas.
                Simule processamento usando:
                Thread.sleep(10);
                Compare o tempo com a execução sequencial.
                Objetivo:
                entender uso de thread pools
                (aumente e diminual o numero de threads no pool e observe o que acontece)

       TODO Exercício 3 — Agrupar vendas por cliente (paralelo)
                tilizando parallelStream(), agrupe as vendas por cliente.
                Resultado esperado:
                Cliente -> quantidade de vendas
                Dica:
                Collectors.groupingBy(...)
                Objetivo:
                aplicar paralelismo em operações de agregação

        TODO Exercício bônus — Encontrar cliente que mais comprou
                Utilizando Streams, descubra qual cliente possui o maior valor total de compras.
                Resultado esperado:
                Cliente com maior total de compras
                Objetivo:
                combinar groupingBy + summingDouble + max
     */

    public static void exercicios(List<Venda> vendas){
        Map<String, Double> totalPorCliente =
                vendas.stream()
                        .collect(Collectors.groupingBy(
                                v -> v.getCliente().getNome(),
                                Collectors.summingDouble(Venda::getValorTotal)
                        ));
        totalPorCliente.forEach((k, v) -> System.out.println(k + " - " + v));

        Optional<Map.Entry<String, Double>> maiorCliente =
                totalPorCliente.entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue());

        maiorCliente.ifPresent(System.out::println);


        vendas.stream()
                /*
                    1️⃣ Criar um Stream de vendas
                 */
                .collect(Collectors.groupingBy(
                        v -> v.getCliente().getNome(),
                        Collectors.summingDouble(Venda::getValorTotal)
                ))
                /*
                    2️⃣ Agrupar vendas por cliente e somar valores
                 */
                .entrySet()
                /*
                    3️⃣ Transformar o Map em uma coleção de entradas
                       Map<String, Double>
                               ↓
                       Set<Map.Entry<String, Double>>
                       entrySet() converte o mapa em uma coleção de entries (registro). cada registro seria (cliente -> total)
                       ex:
                       (Maria, 8200)
                       (Joao, 6400)
                       (Ana, 7100)
                */
                .stream()
                /*
                     4️⃣ Criar um Stream das entradas
                      Set<Map.Entry<String, Double>>
                      ↓
                      Stream<Map.Entry<String, Double>>
                */
                .max(Map.Entry.comparingByValue())
                /*
                    5️⃣ Encontrar o maior valor
                    Estamos a procurar o registro com o maior valor
                    Java compara o registro com o maior valor, mas antes ele converte para o map, e usa o value do map
                    ex:
                    Maria -> 8200
                    Joao  -> 6400
                    Ana   -> 7100
                    O resultado to max vai ser um Optional<Map.Entry<String, Double>>
                */
                // 6️⃣ Exibir o resultado
                .ifPresent(e ->
                        System.out.println("Cliente com maior total: " + e.getKey() + " -> " + e.getValue()));

        System.out.println("Clientes com venda maior que 5000:");
        vendas.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getCliente().getNome(),
                        Collectors.summingDouble(Venda::getValorTotal)
                ))
                .entrySet()
                .stream()
                .filter(e -> e.getValue() > 5000)
                .forEach(System.out::println);

        /*
            SELECT cliente, SUM(valor)
            FROM vendas
            GROUP BY cliente
            HAVING SUM(valor) > 5000

            vendas
               ↓
            filter(valor > 1000)   → WHERE
               ↓
            groupingBy(cliente)    → GROUP BY
               ↓
            filter(total > 5000)   → HAVING
         */
    }
}
