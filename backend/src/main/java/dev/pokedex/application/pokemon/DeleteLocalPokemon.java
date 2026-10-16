package dev.pokedex.application.pokemon;

import dev.pokedex.domain.exception.PokemonNotFoundException;
import dev.pokedex.domain.port.PokemonRepository;

public class DeleteLocalPokemon {

    private final PokemonRepository repository;

    public DeleteLocalPokemon(PokemonRepository repository) {
        this.repository = repository;
    }

    public void execute(int id) {
        if (!repository.deleteById(id)) {
            throw new PokemonNotFoundException(String.valueOf(id));
        }
    }
}
