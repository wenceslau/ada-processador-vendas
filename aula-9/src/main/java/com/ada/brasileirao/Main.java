package com.ada.brasileirao;

import com.ada.brasileirao.model.Cartao;
import com.ada.brasileirao.model.Gol;
import com.ada.brasileirao.model.Jogo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {

        /*
            campeonato-brasileiro-full.csv
            campeonato-brasileiro-gols.csv
            campeonato-brasileiro-cartoes.csv

            ⚽ 1. Time que mais venceu em 2008
            🗺️ 2. Estado com menos jogos (2003–2022)
            🥅 3. Jogador com mais gols
            🎯 4. Jogador com mais gols de pênaltis
            🔁 5. Jogador com mais gols contra
            📒 6. Jogador com mais cartões amarelos
            📕 7. Jogador com mais cartões vermelhos
            ⚽🔥 8. Partida com mais gols
            ⚽9. Quem marcou os gols da partida com mais gols
            📕 📒 10. Jogo com mais cartões, vermelhos e amarelos
            ⚽ 11. Top 3 artilheiros
            🏆 12. Top 3 artilheiros em 2022

         */

        // Importar as listas de dados
        List<Jogo> jogos = importarJogos();
        List<Gol> gols = importarGols();
        List<Cartao> cartoes = importarCartoes();

        //region ⚽ 1. Time que mais venceu em 2008
        System.out.println("⚽ Time que mais venceu em 2008:");
        Map<String, Long> vitorias =
                jogos.stream() // Inicia o fluxo de dados dos jogos
                        .filter(j -> j.ano() == 2008) // Filtra apenas os jogos ocorridos no ano de 2008
                        .flatMap(j -> { // Transforma cada jogo no nome do time vencedor (ignorando empates)
                            if (j.golsMandante() > j.golsVisitante()) {
                                return Stream.of(j.mandante());
                            } else if (j.golsVisitante() > j.golsMandante()) {
                                return Stream.of(j.visitante());
                            } else {
                                return Stream.empty(); // empate
                            }
                        })
                        .collect(Collectors.groupingBy( // Agrupa pelo nome do time e contabiliza o total de vitórias
                                time -> time,
                                Collectors.counting()
                        ));

        // Using map instead of flatMap just for explanation
        Map<String, Long> vitorias2 =
                jogos.stream() // Inicia o fluxo de dados dos jogos
                        .filter(j -> j.ano() == 2008) // Filtra apenas os jogos ocorridos no ano de 2008
                        .map(j -> { // Transforma cada jogo no nome do time vencedor (ignorando empates)
                            if (j.golsMandante() > j.golsVisitante()) {
                                return j.mandante();
                            } else if (j.golsVisitante() > j.golsMandante()) {
                                return j.visitante();
                            } else {
                                return null; // empate
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.groupingBy( // Agrupa pelo nome do time e contabiliza o total de vitórias
                                time -> time,
                                Collectors.counting()
                        ));
         /*
            map → sempre retorna 1 valor
            ["Flamengo", "Palmeiras", null, "Santos", null]

            flatMap → pode retornar 0, 1 ou vários valores
            ["Flamengo", "Palmeiras", "Santos"]

            “Aqui nesse examplo eu não tenho sempre um resultado. Às vezes tem vencedor, às vezes não.
             Então eu preciso de flatMap, porque ele permite retornar zero ou um elemento. e o retorno Stream.empty()
             no proximo fluxo, por ser um empty (vazio) ele não é incluido
             ”
         */

        vitorias.entrySet().stream() // Inicia o fluxo a partir das entradas do mapa de vitórias
                .max(Map.Entry.comparingByValue()) // Encontra o registro com a maior quantidade de vitórias
                .ifPresent(System.out::println); // Exibe o resultado na tela caso ele exista
        System.out.println("----------------------------------------------");
        //endregion

        //region 🗺️ 2. Estado com menos jogos (2003–2022)
        System.out.println("\n️\uD83D\uDDFA️ Estado com menos jogos (2003–2022):");
        jogos.stream() // Inicia o fluxo de dados dos jogos
                .filter(j -> j.ano() >= 2003 && j.ano() <= 2022) // Filtra os jogos pelo período de 2003 a 2022
                .collect(Collectors.groupingBy( // Agrupa os jogos por estado e conta a quantidade
                        Jogo::estado,
                        Collectors.counting()
                ))
                .entrySet().stream() // Inicia o fluxo com as entradas do mapa agrupado
                .min(Map.Entry.comparingByValue()) // Encontra o estado com a menor quantidade de jogos
                .ifPresent(System.out::println); // Imprime o resultado se estiver presente
        System.out.println("----------------------------------------------");
        //endregion

        //region 🥅 3. Jogador com mais gols
        System.out.println("\n\uD83E\uDD45 Jogador com mais gols:");
        gols.stream() // Inicia o fluxo de dados de gols
                .collect(Collectors.groupingBy( // Agrupa os gols por jogador e faz a contagem
                        Gol::jogador,
                        Collectors.counting()
                ))
                .entrySet().stream() // Inicia o fluxo com os resultados do agrupamento
                .max(Map.Entry.comparingByValue()) // Identifica o jogador com o maior número de gols
                .ifPresent(e ->
                        System.out.println("Jogador: " + e.getKey() + " - Gols: " + e.getValue())
                ); // Exibe o resultado se presente
        System.out.println("----------------------------------------------");
        //endregion

        //region 🎯 4. Jogador com mais gols de pênalti
        System.out.println("\n\uD83C\uDFAF Jogador com mais gols de pênaltis:");
        gols.stream() // Inicia o fluxo de dados de gols
                .filter(g -> "penalty".equalsIgnoreCase(g.tipo())) // Aplica o filtro para obter apenas os gols de pênalti
                .collect(Collectors.groupingBy( // Agrupa os gols de pênalti por jogador e conta
                        Gol::jogador,
                        Collectors.counting()
                ))
                .entrySet().stream() // Inicia o fluxo com as contagens por jogador
                .max(Map.Entry.comparingByValue()) // Encontra o jogador com mais gols de pênalti
                .ifPresent(e ->
                        System.out.println("Jogador: " + e.getKey() + " - Gols: " + e.getValue())
                ); // Exibe o resultado se presente
        System.out.println("----------------------------------------------");
        //endregion

        //region 🔁 5. Jogador com mais gols contra
        System.out.println("\n\uD83D\uDD01 Jogador com mais gols contra:");
        gols.stream() // Inicia o fluxo de dados de gols
                .filter(g -> "Gol Contra".equalsIgnoreCase(g.tipo())) // Filtra exclusivamente os gols contra
                .collect(Collectors.groupingBy( // Agrupa os gols contra por jogador e contabiliza
                        Gol::jogador,
                        Collectors.counting()
                ))
                .entrySet().stream() // Inicia o fluxo a partir das entradas do mapa
                .max(Map.Entry.comparingByValue()) // Busca o jogador com o maior número de gols contra
                .ifPresent(e ->
                        System.out.println("Jogador: " + e.getKey() + " - Gols: " + e.getValue())
                ); // Exibe o resultado se presente
        System.out.println("----------------------------------------------");
        //endregion

        //region 📒 6. Jogador com mais cartões amarelos
        System.out.println("\n\uD83D\uDCD2 Jogador com mais cartões amarelos:");
        cartoes.stream() // Inicia o fluxo de dados de cartões
                .filter(c -> "amarelo".equalsIgnoreCase(c.tipo())) // Filtra apenas os cartões amarelos
                .collect(Collectors.groupingBy( // Agrupa por jogador e soma a quantidade de cartões
                        Cartao::jogador,
                        Collectors.counting()
                ))
                .entrySet().stream() // Inicia o fluxo com os totais de cartões por jogador
                .max(Map.Entry.comparingByValue()) // Obtém o jogador com o maior número de cartões amarelos
                .ifPresent(e ->
                        System.out.println("Jogador: " + e.getKey() + " - Cartões: " + e.getValue())
                ); // Exibe o resultado se presente
        System.out.println("----------------------------------------------");
        //endregion

        //region 📕 7. Jogador com mais cartões vermelhos
        System.out.println("\n\uD83D\uDCD5 Jogador com mais cartões vermelhos:");
        cartoes.stream() // Inicia o fluxo de dados de cartões
                .filter(c -> "vermelho".equalsIgnoreCase(c.tipo())) // Filtra apenas os cartões vermelhos
                .collect(Collectors.groupingBy( // Agrupa os cartões vermelhos por jogador e os conta
                        Cartao::jogador,
                        Collectors.counting()
                ))
                .entrySet().stream() // Inicia o fluxo a partir do mapa gerado
                .max(Map.Entry.comparingByValue()) // Encontra o jogador com a maior contagem
                .ifPresent(e ->
                        System.out.println("Jogador: " + e.getKey() + " - Cartões: " + e.getValue())
                ); // Exibe o resultado se presente
        System.out.println("----------------------------------------------");
        //endregion

        //region  ⚽🔥 8. Partida com mais gols
        System.out.println("\n⚽\uD83D\uDD25 Partida com mais gols:");
        jogos.stream() // Inicia o fluxo de dados dos jogos
                .max(Comparator.comparingInt( // Localiza a partida com a maior soma de gols
                        j -> j.golsMandante() + j.golsVisitante()
                ))
                .ifPresent(j -> // Caso exista a partida, imprime os detalhes do placar
                        System.out.println(j.id() + " - "
                                           + j.mandante() + " "
                                           + j.golsMandante() + " x "
                                           + j.golsVisitante() + " "
                                           + j.visitante())
                );
        System.out.println("----------------------------------------------");
        //endregion

        //region  ⚽ 9. Quem marcou os gols da partida com mais gols
        System.out.println("\n⚽Quem marcou os gols da partida com mais gols:");
        jogos.stream() // Inicia o fluxo de dados dos jogos
                .max(Comparator.comparingInt( // Busca o jogo com mais gols marcados
                        j -> j.golsMandante() + j.golsVisitante()
                )).ifPresent(j -> gols.stream() // Se a partida existir, inicia o fluxo na lista de gols
                        .filter(g -> g.idJogo().equals(j.id())) // Filtra os gols que pertencem ao ID dessa partida
                        .forEach(System.out::println)); // Itera e imprime cada gol encontrado
        System.out.println("----------------------------------------------");
        //endregion

        //region 📕📒 10. Jogo com mais cartões, vermelhos e amarelos
        System.out.println("\n\uD83D\uDCD5 \uD83D\uDCD2 Jogo com mais cartões, vermelhos e amarelos:");
        cartoes.stream() // Inicia o fluxo de dados de cartões
                .collect(Collectors.groupingBy( // Agrupa os cartões pelo ID do jogo e os conta
                        Cartao::idJogo,
                        Collectors.counting()))
                .entrySet().stream() // Inicia o fluxo com as entradas do mapa agrupado
                .max(Map.Entry.comparingByValue()) // Encontra o ID do jogo com a maior quantidade de cartões
                .ifPresent(c -> jogos.stream() // Se houver resultado, inicia o fluxo na lista de jogos
                        .filter(j -> j.id().equals(c.getKey())) // Filtra os jogos para achar aquele com o ID correspondente
                        .forEach(System.out::println)); // Itera e imprime os detalhes do jogo
        System.out.println("----------------------------------------------");
        //endregion

        //region  ⚽ 11. Top 3 artilheiros
        System.out.println("\n⚽ Top 3 artilheiros:");
        gols.stream()
                .collect(Collectors.groupingBy(
                        Gol::jogador,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .forEach(e ->
                        System.out.println(
                                e.getKey() + " → " + e.getValue() + " gols"
                        )
                );
        System.out.println("----------------------------------------------");
        //endregion

        //region  🏆 Top 3 artilheiros em 2022
        System.out.println("\n🏆 Top 3 artilheiros em 2022:");
        Map<String, Jogo> jogosPorId =
                jogos.stream()
                        .collect(Collectors.toMap(
                                Jogo::id,
                                j -> j
                        )); // Criar um Map apenas para uma consulta mais rápida.

        gols.stream()
                // JOIN + filtro no Map por ano
                .filter(g -> {
                    Jogo jogo = jogosPorId.get(g.idJogo()); // “isso é O(1), super rápido”
                    return jogo != null && jogo.ano() == 2022;
                })
                // GROUP BY jogador
                .collect(Collectors.groupingBy(
                        Gol::jogador,
                        Collectors.counting()
                ))
                // ordenar
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()) // inverte a ordenação
                .limit(3) // pega apenas os 3 primeiros
                .forEach(e ->
                        System.out.println(e.getKey() + " → " + e.getValue() + " gols")
                );

        // Filtro com join mas sem o map, apenas para comparação
        gols.stream()
                // JOIN + filtro direto na lista de jogos por ano “isso é O(N), percorre a lista toda, mais lento”
                .filter(g -> jogos.stream().anyMatch(j -> j.id().equals(g.idJogo()) && j.ano() == 2008))
                // GROUP BY jogador
                .collect(Collectors.groupingBy(
                        Gol::jogador,
                        Collectors.counting()
                ))
                // ordenar
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .forEach(e ->
                        System.out.println(e.getKey() + " → " + e.getValue() + " gols")
                );

        /*
            O(1) → tempo constante (sempre rápido)
            O(n) → depende do tamanho da lista

            O(1) → acesso direto
            O(n) → procura
         */

        //endregion

        /*
            “Acabamos de fazer análise de dados com Java, usando programação funcional.”
        */

    }

    // Métodos de importação

    public static List<Jogo> importarJogos() throws IOException {
        Path arquivo = Paths.get("files/campeonato-brasileiro-full.csv");

        return Files.lines(arquivo) // Lê as linhas do arquivo gerando um fluxo de strings
                .skip(1) // Pula a primeira linha referente ao cabeçalho
                .map(linha -> { // Mapeia a string e converte em um objeto Jogo

                    String[] dados = extrairDados(linha);
                    String id = extrairString(dados, 0);
                    String timeMandante = extrairString(dados, 4);
                    String timeVisitante = extrairString(dados, 5);
                    Integer ano = extrairAno(dados, 2);
                    Integer golsMandante = extrairNumero(dados, 12);
                    Integer golsVisitante = extrairNumero(dados, 13);
                    String estado = extrairString(dados, 14);

                    return new Jogo(id, ano, timeMandante, timeVisitante, golsMandante, golsVisitante, estado);
                })
                .toList(); // Finaliza a operação coletando os dados em uma Lista

    }

    public static List<Gol> importarGols() throws IOException {
        Path arquivo = Paths.get("files/campeonato-brasileiro-gols.csv");

        return Files.lines(arquivo) // Lê as linhas do arquivo e retorna um fluxo de strings
                .skip(1) // Ignora o cabeçalho do arquivo CSV
                .map(linha -> { // Mapeia a linha de texto para convertê-la em um objeto Gol
                    String[] dados = extrairDados(linha);
                    String idJogo = extrairString(dados, 0);
                    String jogador = extrairString(dados, 3);
                    String time = extrairString(dados, 2);
                    String tipoGol = extrairString(dados, 5);
                    return new Gol(idJogo, jogador, time, tipoGol);
                })
                .toList(); // Converte o fluxo resultante em uma Lista
    }

    public static List<Cartao> importarCartoes() throws IOException {
        Path arquivo = Paths.get("files/campeonato-brasileiro-cartoes.csv");

        return Files.lines(arquivo) // Lê as linhas do arquivo e retorna um fluxo de strings
                .skip(1) // Ignora a primeira linha (cabeçalho)
                .map(linha -> { // Mapeia cada linha, transformando o texto em um objeto Cartao
                    String[] dados = extrairDados(linha);
                    String idJogo = extrairString(dados, 0);
                    String time = extrairString(dados, 2);
                    String jogador = extrairString(dados, 4);
                    String tipo = extrairString(dados, 3);
                    return new Cartao(idJogo, time, jogador, tipo);
                })
                .toList(); // Coleta os elementos processados e retorna como uma Lista
    }


    // Métodos utilitários

    private static String[] extrairDados(String linha) {
        return linha.split(",");
    }

    private static String extrairString(String[] dados, int index) {
        if (dados.length > index) {
            return dados[index].trim().replaceAll("\"", "");
        }
        return null;
    }

    private static Integer extrairNumero(String[] dados, int index) {
        String valor = extrairString(dados, index);
        if (valor == null || valor.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer extrairAno(String[] dados, int index) {
        String valor = extrairString(dados, index);
        if (valor == null) {
            return null;
        }
        String[] array = valor.split("/");
        if (array.length == 3) {
            return extrairNumero(array, 2);
        }
        return null;
    }

}
