package com.ada.brasileirao.model;

public record Jogo(
        String id,
        int ano,
        String mandante,
        String visitante,
        int golsMandante,
        int golsVisitante,
        String estado
) {
}
