package dev.pokedex.application.pokemon;

import dev.pokedex.domain.exception.PokemonNotFoundException;
import dev.pokedex.domain.model.Pokemon;
import dev.pokedex.domain.port.PokemonRepository;

public class GetLocalPokemon {

    private final PokemonRepository repository;

    public GetLocalPokemon(PokemonRepository repository) {
        this.repository = repository;
    }

    public Pokemon execute(int id) {
        return repository.findById(id).orElseThrow(() -> new PokemonNotFoundException(String.valueOf(id)));
    }
}
