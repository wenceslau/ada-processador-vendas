package com.ada.brasileirao.model;

public record Jogo(
        String id,
        Integer ano,
        String mandante,
        String visitante,
        Integer golsMandante,
        Integer golsVisitante,
        String estado
) {
}
