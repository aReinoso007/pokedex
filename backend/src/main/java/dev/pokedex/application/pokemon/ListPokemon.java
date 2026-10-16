package dev.pokedex.application.pokemon;

import dev.pokedex.domain.model.PageResult;
import dev.pokedex.domain.model.PokemonSummary;
import dev.pokedex.domain.port.PokemonCatalogPort;

/** US01: browse remote Pokemon with pagination. */
public class ListPokemon {

    public static final int MAX_PAGE_SIZE = 50;

    private final PokemonCatalogPort catalog;

    public ListPokemon(PokemonCatalogPort catalog) {
        this.catalog = catalog;
    }

    public PageResult<PokemonSummary> execute(int page, int size) {
        Paging.validate(page, size, MAX_PAGE_SIZE);
        return catalog.list(page, size);
    }
}
