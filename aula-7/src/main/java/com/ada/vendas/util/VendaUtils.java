package com.ada.vendas.util;

import com.ada.vendas.model.Cliente;
import com.ada.vendas.model.Venda;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class VendaUtils {

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

}
