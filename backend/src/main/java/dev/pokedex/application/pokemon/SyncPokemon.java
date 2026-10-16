package dev.pokedex.application.pokemon;

import dev.pokedex.domain.exception.PokemonAlreadySyncedException;
import dev.pokedex.domain.model.Pokemon;
import dev.pokedex.domain.port.PokemonCatalogPort;
import dev.pokedex.domain.port.PokemonRepository;

/** US03: replicate a remote Pokemon into the local store. */
public class SyncPokemon {

    private final PokemonCatalogPort catalog;
    private final PokemonRepository repository;

    public SyncPokemon(PokemonCatalogPort catalog, PokemonRepository repository) {
        this.catalog = catalog;
        this.repository = repository;
    }

    public Pokemon execute(String idOrName) {
        Pokemon remote = catalog.fetch(Paging.normalizeIdentifier(idOrName));
        if (repository.existsById(remote.id())) {
            throw new PokemonAlreadySyncedException(remote.id());
        }
        return repository.save(remote);
    }
}
