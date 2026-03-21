package com.ada.vendas;

import com.ada.vendas.model.Venda;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReviewJavaTime {

    public static void main(String[] args) {

        //📌 1. Criando datas
        LocalDate hoje = LocalDate.now();
        LocalDate dataEspecifica = LocalDate.of(2026, 3, 21);

        System.out.println(hoje);
        System.out.println(dataEspecifica);
        /*------------------------------------------------------------------------------------------------*/

        //📌 2. Criando data e hora
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataHora = LocalDateTime.of(2026, 3, 21, 10, 30);

        System.out.println(agora);
        System.out.println(dataHora);
        /*------------------------------------------------------------------------------------------------*/

        //📌 3. Trabalhando com tempo (hora)
        LocalTime hora = LocalTime.now();
        LocalTime horaEspecifica = LocalTime.of(14, 45);

        System.out.println(hora);
        System.out.println(horaEspecifica);
        /*------------------------------------------------------------------------------------------------*/

        //📌 4. Somando e subtraindo datas
        LocalDate hoje1 = LocalDate.now();

        LocalDate amanha = hoje1.plusDays(1);
        LocalDate semanaPassada = hoje1.minusWeeks(1);

        System.out.println(amanha);
        System.out.println(semanaPassada);
        /*------------------------------------------------------------------------------------------------*/

        //📌 5. Comparando datas
        LocalDate hoje2 = LocalDate.now();
        LocalDate outraData = LocalDate.of(2026, 1, 1);

        System.out.println(hoje2.isAfter(outraData));
        System.out.println(hoje2.isBefore(outraData));
        System.out.println(hoje2.isEqual(outraData));
        /*------------------------------------------------------------------------------------------------*/

        //📌 6. Diferença entre datas
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.now();

        Period periodo = Period.between(inicio, fim);

        System.out.println(periodo.getYears());
        System.out.println(periodo.getMonths());
        System.out.println(periodo.getDays());
        /*------------------------------------------------------------------------------------------------*/

        //📌 7. Trabalhando com Duration (tempo)
        LocalTime inicio1 = LocalTime.of(10, 0);
        LocalTime fim1 = LocalTime.of(12, 30);

        Duration duracao = Duration.between(inicio1, fim1);

        System.out.println(duracao.toMinutes());
        System.out.println(duracao.toHours());
        /*------------------------------------------------------------------------------------------------*/

        //📌 8. Formatando datas
        LocalDate hoje3 = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String dataFormatada = hoje3.format(formatter);

        System.out.println(dataFormatada);
        /*------------------------------------------------------------------------------------------------*/

        //📌 9. Convertendo String para data
        String dataTexto = "21/03/2026";

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate data = LocalDate.parse(dataTexto, formatter1);

        System.out.println(data);
        /*------------------------------------------------------------------------------------------------*/

        //📌 10. Trabalhando com Month
        LocalDate data1 = LocalDate.now();

        Month mes = data1.getMonth();

        System.out.println(mes);
        System.out.println(mes.getValue());

        //📌 11. Exemplo aplicado ao projeto (vendas)
        List<Venda> vendas = new ArrayList<>();
        vendas.stream()
                .filter(v -> v.getDataVenda().isAfter(LocalDate.of(2026, 1, 1)))
                .forEach(System.out::println);
    }

}
