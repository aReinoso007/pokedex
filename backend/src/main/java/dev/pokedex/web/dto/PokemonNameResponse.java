package dev.pokedex.web.dto;

import dev.pokedex.domain.model.PokemonName;

public record PokemonNameResponse(int id, String name, String spriteUrl) {

    public static PokemonNameResponse from(PokemonName name) {
        return new PokemonNameResponse(name.id(), name.name(), name.spriteUrl());
    }
}
