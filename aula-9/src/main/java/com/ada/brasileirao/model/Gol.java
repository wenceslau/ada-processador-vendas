package com.ada.brasileirao.model;

public record Gol(
        String idJogo,
        String jogador,
        String time,
        String tipo // normal, penalty, own_goal
) {

}
