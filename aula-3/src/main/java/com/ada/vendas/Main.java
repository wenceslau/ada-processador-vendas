package com.ada.vendas;

import com.ada.vendas.model.Cliente;
import com.ada.vendas.model.Produto;
import com.ada.vendas.model.Venda;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {

        List<Venda> vendas = gerarVendasExemplo();

        LocalDate data = LocalDate.now().minusDays(7);
        List<Venda> vendasAposData = filtrarVendas(vendas, v -> v.getDataVenda().isAfter(data));
        imprimirVendas(vendasAposData);

        List<Venda> vendasCaras = filtrarVendas(vendas, v -> v.getValorTotal() > 1000);
        imprimirVendas(vendasCaras);

        LocalDate inicio = LocalDate.now().minusDays(7);
        LocalDate fim = LocalDate.now();
        Predicate<Venda> regra = v ->
                (v.getDataVenda().isEqual(inicio) || v.getDataVenda().isAfter(inicio)) &&
                (v.getDataVenda().isEqual(fim) || v.getDataVenda().isBefore(fim));
        List<Venda> vendasEntreDatas = filtrarVendas(vendas, regra);
        imprimirVendas(vendasEntreDatas);


        List<Venda> vendasDoMesAtual = filtrarVendas(
                vendas,
                v -> v.getDataVenda().getMonth() == LocalDate.now().getMonth() &&
                     v.getDataVenda().getYear() == LocalDate.now().getYear()
        );
        imprimirVendas(vendasDoMesAtual);


        List<Venda> vendasUltimos7Dias = filtrarVendas(vendas, regraVendasUltimos7Dias());
        imprimirVendas(vendasUltimos7Dias);


        List<Venda> vendasAnoAtual = filtrarVendas(vendas, regraVendasAnoAtual());
        imprimirVendas(vendasAnoAtual);


        // X-POINT Exercicios
        List<Venda> vendasQuantidadeMaior2 =
                filtrarVendas(vendas, v -> v.getQuantidade() > 2);
        imprimirVendas(vendasQuantidadeMaior2);


        LocalDate hoje = LocalDate.now();
        List<Venda> vendasMesAtual2 =
                filtrarVendas(vendas,
                        v -> v.getDataVenda().getMonth() == hoje.getMonth()
                             && v.getDataVenda().getYear() == hoje.getYear());
        imprimirVendas(vendasMesAtual2);


        List<Venda> vendasEletronicos =
                filtrarVendas(vendas,
                        v -> v.getProduto().getCategoria().equals("Eletrônicos"));
        imprimirVendas(vendasEletronicos);


        List<Venda> vendasRecentesEValorAlto =
                filtrarVendas(vendas,
                        v -> v.getValorTotal() > 500 &&
                             v.getDataVenda().isAfter(LocalDate.now().minusDays(30)));
        imprimirVendas(vendasRecentesEValorAlto);


        Optional<Venda> venda = buscarVendaPorId(vendas, 3L);

        if (venda.isPresent()) {
            System.out.println(venda.get().getValorTotal());
        }

        buscarVendaPorId(vendas, 3L).ifPresent(v -> System.out.println(v.getValorTotal()));


        //Imprimir venda
        processarVendas(vendas, v -> System.out.println("Venda: " + v.getId()));

        // Imprimir valor total
        processarVendas(vendas, v -> System.out.println(v.getValorTotal()));


        Function<Venda, Cliente> functionClientesVendaAlta = v -> (v.getValorTotal()> 1000 ? v.getCliente() : null);
        List<Cliente> valoresAlto =  transformarVendas(vendas, functionClientesVendaAlta);
        System.out.println("Vendas Valores alto: " + valoresAlto);
        valoresAlto.forEach(System.out::println);


        List<Cliente> valores =  transformarVendas(vendas, v -> v.getCliente());



        //Exercicios

        processarVendas(vendas, v -> {
            System.out.println("Cliente: " + v.getCliente().getNome());
            System.out.println("Produto: " + v.getProduto().getNome());
            System.out.println("Valor Total: " + v.getValorTotal());
            System.out.println("------------------------");
        });

        List<String> nomesProdutos =
                transformarVendasGenerico(vendas,
                        v -> v.getProduto().getNome());

        nomesProdutos.forEach(System.out::println);


        Produto produto1 = new Produto(1L, "Notebook", "Eletrônicos");
        Produto produto2 = new Produto(2L, "Mouse", "Eletrônicos");
        Produto produto3 = new Produto(3L, "Teclado", "Eletrônicos");
        Produto produto4 = new Produto(4L, "Cadeira", "Móveis");
        List<Produto> produtos = Arrays.asList(produto1, produto2, produto3, produto4);
        Optional<Produto> produto = buscarProdutoPorId(produtos, 1L);

        produto.ifPresent(p ->
                System.out.println("Produto encontrado: " + p.getNome()));


        // forma 1
        Optional<Venda> venda1 = buscarVendaPorId(vendas, 3L);
        venda1.ifPresent(v ->
                System.out.println("Venda encontrada: " + v.getId()));

        // forma 2
        Optional<Venda> venda2 = buscarVendaPorId(vendas, 3L);
        if (venda2.isPresent()) {
            System.out.println("Venda encontrada: " + venda2.get().getId());
        } else {
            System.out.println("Venda não encontrada.");
        }

        // forma 3
        buscarVendaPorId(vendas, 3L)
                .ifPresent(v -> System.out.println("Venda encontrada: " + v.getId()));

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

    public static List<Venda> filtrarVendas(List<Venda> vendas, Predicate<Venda> regra) {

        List<Venda> resultado = new ArrayList<>();

        for (Venda venda : vendas) {
            if (regra.test(venda)) {
                resultado.add(venda);
            }
        }

        return resultado;
    }

    public static Predicate<Venda> regraVendasUltimos7Dias() {

        LocalDate hoje = LocalDate.now();
        LocalDate seteDiasAtras = hoje.minusDays(7);

        return v -> v.getDataVenda().isEqual(seteDiasAtras) || v.getDataVenda().isAfter(seteDiasAtras);

    }

    public static Predicate<Venda> regraVendasAnoAtual() {

        int anoAtual = LocalDate.now().getYear();

        return v -> v.getDataVenda().getYear() == anoAtual;
    }

    public static void imprimirVendas(List<Venda> vendas) {
        for (Venda venda : vendas) {
            System.out.println(venda);
        }
    }

    public static Optional<Venda> buscarVendaPorId(List<Venda> vendas, Long id) {

        for (Venda venda : vendas) {
            if (venda.getId().equals(id)) {
                return Optional.of(venda);
            }
        }

        return Optional.empty();
    }

    public static void processarVendas(List<Venda> vendas, Consumer<Venda> acao) {

        /*
        Consumer representa uma ação que recebe um objeto e não retorna valor.
         */

        for (Venda venda : vendas) {
            acao.accept(venda);
        }
    }

    public static List<Cliente> transformarVendas(List<Venda> vendas, Function<Venda, Cliente> funcao) {

        /*
        Function representa uma transformação. Ela recebe um valor e retorna outro.
         */

        Set<Cliente> resultado = new HashSet<>();

        for (Venda venda : vendas) {
            resultado.add(funcao.apply(venda));
        }

        return new ArrayList<>(resultado);
    }



    /*

        Predicate	Testa condição - boolean
        Consumer	Executa ação - sem retorno
        Function	Transforma valor - recebe valor e retorna valor
        Optional    Valor pode existir ou não

     */

    /*

        TODO Exercício 1: Criar um método que utilize Consumer para imprimir:
            cliente
            produto
            valor total

        TODO Exercício 2: Usar Function para gerar uma lista contendo apenas nomes dos produtos

        TODO Exercício 3: Criar método que busque produto por id retornando Optional.

        TODO Exercício :  (Desafio) Usar Optional para imprimir venda somente se ela existir.

     */

    public static <R> List<R> transformarVendasGenerico(
            List<Venda> vendas,
            Function<Venda, R> funcao) {

        List<R> resultado = new ArrayList<>();

        for (Venda venda : vendas) {
            resultado.add(funcao.apply(venda));
        }

        return resultado;
    }

    public static Optional<Produto> buscarProdutoPorId(
            List<Produto> produtos,
            Long id) {

        for (Produto produto : produtos) {
            if (produto.getId().equals(id)) {
                return Optional.of(produto);
            }
        }

        return Optional.empty();
    }
}
