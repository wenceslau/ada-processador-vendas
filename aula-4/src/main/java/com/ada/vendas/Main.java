package com.ada.vendas;

import com.ada.vendas.model.Cliente;
import com.ada.vendas.model.Produto;
import com.ada.vendas.model.Venda;
import com.ada.vendas.util.VendaUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        List<Venda> vendas = gerarVendasExemplo();

        // 1️⃣ Filter – Filtrar vendas por valor
        List<Venda> vendasCaras =
                vendas.stream()                                             // stream → cria fluxo
                        .filter(v -> v.getValorTotal() > 1000)       // filter → aplica Predicate
                        .toList();                                          // toList → coleta resultado
        vendasCaras.forEach(System.out::println);

        // 2️⃣ Filter – Vendas dos últimos 7 dias
        List<Venda> vendasRecentes =
                vendas.stream()
                        .filter(v -> v.getDataVenda()
                                .isAfter(LocalDate.now().minusDays(7)))
                        .toList();
        vendasRecentes.forEach(System.out::println);


        // 3️⃣ Map – Obter valores das vendas
        List<Double> valores =
                vendas.stream()
                        .map(v -> v.getValorTotal())
                        //.map(Venda::getValorTotal)
                        .toList();
        valores.forEach(System.out::println);


        // 4️⃣ Map – Obter nomes dos clientes
        List<String> clientes =
                vendas.stream()
                        .map(v -> v.getCliente().getNome())
                        .toList();
        clientes.forEach(System.out::println);


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
        ordenadas.forEach(System.out::println);

        List<Venda> ordenadasDesc =
                vendas.stream()
                        .sorted(Comparator.comparing(Venda::getValorTotal).reversed())
                        .toList();
        ordenadasDesc.forEach(System.out::println);


        // 7️⃣ Distinct – Clientes únicos
        List<String> clientes2 =
                vendas.stream()
                        .map(v -> v.getCliente().getNome())
                        .distinct()
                        .toList();
        clientes2.forEach(System.out::println);


        // 8️⃣ Limit – Top 3 vendas
        List<Venda> top3 =
                vendas.stream()
                        .sorted(Comparator.comparing(Venda::getValorTotal).reversed())
                        .limit(3)
                        .toList();
        top3.forEach(System.out::println);



        // 9️⃣ Counting – Quantidade de vendas maior que 1000
        long total =
                vendas.stream()
                        .filter(v-> v.getValorTotal() > 1000)
                        .count();
        System.out.println(total);


        // 🔟 SummingDouble – Total vendido
        double totalVendas =
                vendas.stream()
                        .mapToDouble(Venda::getValorTotal)
                        .sum();
        System.out.println(totalVendas);



        // 1️⃣1️⃣ Average – Média das vendas
        double media =
                vendas.stream()
                        .mapToDouble(Venda::getValorTotal)
                        .average()
                        .orElse(0);
        System.out.println(media);


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
        vendasPorCliente.forEach((k, v) -> System.out.println(k + " - " + v));

        // 1️⃣4️⃣ GroupingBy + SummingDouble
        Map<String, Double> totalPorCliente =
                vendas.stream()
                        .collect(
                                Collectors.groupingBy(
                                        v -> v.getCliente().getNome(),
                                        Collectors.summingDouble(Venda::getValorTotal)
                                )
                        );
        totalPorCliente.forEach((k, v) -> System.out.println(k + " - " + v));



        // 1️⃣5️⃣ GroupingBy por categoria
        Map<String, List<Venda>> vendasPorCategoria =
                vendas.stream()
                        .collect(Collectors.groupingBy(
                                v -> v.getProduto().getCategoria()));
        vendasPorCategoria.forEach((k, v) -> System.out.println(k + " - " + v));



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
            1️⃣ Filtrar vendas acima de 500
            2️⃣ Listar nomes dos clientes únicos
            3️⃣ Ordenar vendas por data
            4️⃣ Calcular total vendido
            5️⃣ Agrupar vendas por cliente
     */

    /*
        “Stream não é uma estrutura de dados.
        É uma forma de processar dados.”
     */
}
