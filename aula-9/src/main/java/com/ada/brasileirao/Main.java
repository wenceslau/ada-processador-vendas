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

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws IOException {

        List<Jogo> jogos = importarJogos();
        List<Gol> gols = importarGols();
        List<Cartao> cartoes = importarCartoes();

        //⚽ 1. Time que mais venceu em 2008
        System.out.println("Time que mais venceu em 2008:");
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

        //🗺️ 2. Estado com menos jogos (2003–2022)
        System.out.println("\nEstado com menos jogos (2003–2022):");
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

        //🥅 3. Jogador com mais gols
        System.out.println("\nJogador com mais gols:");
        gols.stream() // Inicia o fluxo de dados de gols
                .collect(Collectors.groupingBy( // Agrupa os gols por jogador e faz a contagem
                        Gol::jogador,
                        Collectors.counting()
                ))
                .entrySet().stream() // Inicia o fluxo com os resultados do agrupamento
                .max(Map.Entry.comparingByValue()) // Identifica o jogador com o maior número de gols
                .ifPresent(System.out::println); // Exibe o resultado se presente
        System.out.println("----------------------------------------------");

        //🎯 4. Jogador com mais gols de pênalti
        System.out.println("\nJogador com mais gols de pênaltis:");
        gols.stream() // Inicia o fluxo de dados de gols
                .filter(g -> "penalty".equalsIgnoreCase(g.tipo())) // Aplica o filtro para obter apenas os gols de pênalti
                .collect(Collectors.groupingBy( // Agrupa os gols de pênalti por jogador e conta
                        Gol::jogador,
                        Collectors.counting()
                ))
                .entrySet().stream() // Inicia o fluxo com as contagens por jogador
                .max(Map.Entry.comparingByValue()) // Encontra o jogador com mais gols de pênalti
                .ifPresent(System.out::println); // Exibe o resultado caso não seja nulo

        //🔁 5. Jogador com mais gols contra
        System.out.println("\nJogador com mais gols contra:");
        gols.stream() // Inicia o fluxo de dados de gols
                .filter(g -> "Gol Contra".equalsIgnoreCase(g.tipo())) // Filtra exclusivamente os gols contra
                .collect(Collectors.groupingBy( // Agrupa os gols contra por jogador e contabiliza
                        Gol::jogador,
                        Collectors.counting()
                ))
                .entrySet().stream() // Inicia o fluxo a partir das entradas do mapa
                .max(Map.Entry.comparingByValue()) // Busca o jogador com o maior número de gols contra
                .ifPresent(System.out::println); // Imprime o resultado encontrado
        System.out.println("----------------------------------------------");

        //📒 6. Jogador com mais cartões amarelos
        System.out.println("\nJogador com mais cartões amarelos:");
        cartoes.stream() // Inicia o fluxo de dados de cartões
                .filter(c -> "amarelo".equalsIgnoreCase(c.tipo())) // Filtra apenas os cartões amarelos
                .collect(Collectors.groupingBy( // Agrupa por jogador e soma a quantidade de cartões
                        Cartao::jogador,
                        Collectors.counting()
                ))
                .entrySet().stream() // Inicia o fluxo com os totais de cartões por jogador
                .max(Map.Entry.comparingByValue()) // Obtém o jogador com o maior número de cartões amarelos
                .ifPresent(System.out::println); // Imprime o resultado se existir
        System.out.println("----------------------------------------------");

        //📕 7. Jogador com mais cartões vermelhos
        System.out.println("\nJogador com mais cartões vermelhos:");
        cartoes.stream() // Inicia o fluxo de dados de cartões
                .filter(c -> "vermelho".equalsIgnoreCase(c.tipo())) // Filtra apenas os cartões vermelhos
                .collect(Collectors.groupingBy( // Agrupa os cartões vermelhos por jogador e os conta
                        Cartao::jogador,
                        Collectors.counting()
                ))
                .entrySet().stream() // Inicia o fluxo a partir do mapa gerado
                .max(Map.Entry.comparingByValue()) // Encontra o jogador com a maior contagem
                .ifPresent(System.out::println); // Imprime o resultado na tela
        System.out.println("----------------------------------------------");

        // ⚽🔥 8. Partida com mais gols
        System.out.println("\nPartida com mais gols:");
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

        // ⚽ 9. Quem marcou os gols da partida com mais gols
        System.out.println("\nQuem marcou os gols da partida com mais gols:");
        jogos.stream() // Inicia o fluxo de dados dos jogos
                .max(Comparator.comparingInt( // Busca o jogo com mais gols marcados
                        j -> j.golsMandante() + j.golsVisitante()
                )).ifPresent(j -> gols.stream() // Se a partida existir, inicia o fluxo na lista de gols
                        .filter(g -> g.idJogo().equals(j.id())) // Filtra os gols que pertencem ao ID dessa partida
                        .forEach(System.out::println)); // Itera e imprime cada gol encontrado
        System.out.println("----------------------------------------------");

        //📕📒 10. Jogo com mais cartões, vermelhos e amarelos
        System.out.println("\nJogo com mais cartões, vermelhos e amarelos:");
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
//        O Estado que teve menos jogos dentro do período 2003 e 2022
//        O jogador que mais fez gols
//        O jogador que mais fez gols de pênaltis
//        O jogador que mais fez gols contras
//        O jogador que mais recebeu cartões amarelos
//        O jogador que mais recebeu cartões vermelhos
//        O placar da partida com mais gols.


    }

    public static List<Jogo> importarJogos() throws IOException {
        Path arquivo = Paths.get("files/campeonato-brasileiro-full.csv");

        return Files.lines(arquivo) // Lê as linhas do arquivo gerando um fluxo de strings
                .skip(1) // Pula a primeira linha referente ao cabeçalho
                .map(linha -> { // Mapeia a string e converte em um objeto Jogo

                    String[] dados = linha.split(",");
                    String id = dados[0].trim().replaceAll("\"", "");
                    String timeMandante = dados[4].trim().replaceAll("\"", "");
                    String timeVisitante = dados[5].trim().replaceAll("\"", "");
                    String ano = dados[2].trim().replaceAll("\"", "").split("/")[2];
                    int golsMandante = Integer.parseInt(dados[12].trim().replaceAll("\"", ""));
                    int golsVisitante = Integer.parseInt(dados[13].trim().replaceAll("\"", ""));
                    String estado = dados[14].trim().replaceAll("\"", "");

                    return new Jogo(id, Integer.parseInt(ano), timeMandante, timeVisitante, golsMandante, golsVisitante, estado);
                })
                .toList(); // Finaliza a operação coletando os dados em uma Lista

    }

    public static List<Gol> importarGols() throws IOException {
        Path arquivo = Paths.get("files/campeonato-brasileiro-gols.csv");

        return Files.lines(arquivo) // Lê as linhas do arquivo e retorna um fluxo de strings
                .skip(1) // Ignora o cabeçalho do arquivo CSV
                .map(linha -> { // Mapeia a linha de texto para convertê-la em um objeto Gol
                    String[] dados = linha.split(",");
                    String idJogo = dados[0].trim().replaceAll("\"", "");
                    String jogador = dados[3].trim().replaceAll("\"", "");
                    String time = dados[2].trim().replaceAll("\"", "");
                    String tipoGol = dados[5].trim().replaceAll("\"", "");
                    return new Gol(idJogo, jogador, time, tipoGol);
                })
                .toList(); // Converte o fluxo resultante em uma Lista
    }

    public static List<Cartao> importarCartoes() throws IOException {
        Path arquivo = Paths.get("files/campeonato-brasileiro-cartoes.csv");

        return Files.lines(arquivo) // Lê as linhas do arquivo e retorna um fluxo de strings
                .skip(1) // Ignora a primeira linha (cabeçalho)
                .map(linha -> { // Mapeia cada linha, transformando o texto em um objeto Cartao
                    String[] dados = linha.split(",");
                    String idJogo = dados[0].trim().replaceAll("\"", "");
                    String time = dados[2].trim().replaceAll("\"", "");
                    String jogador = dados[4].trim().replaceAll("\"", "");
                    String tipo = dados[3].trim().replaceAll("\"", "");
                    return new Cartao(idJogo, time, jogador, tipo);
                })
                .toList(); // Coleta os elementos processados e retorna como uma Lista
    }


}
