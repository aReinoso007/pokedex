package dev.pokedex.application.pokemon;

import dev.pokedex.domain.model.Pokemon;
import dev.pokedex.domain.port.PokemonCatalogPort;

/** US02: full detail (image, stats, description, evolution lineage) of a remote Pokemon. */
public class GetPokemonDetail {

    private final PokemonCatalogPort catalog;

    public GetPokemonDetail(PokemonCatalogPort catalog) {
        this.catalog = catalog;
    }

    public Pokemon execute(String idOrName) {
        return catalog.fetch(Paging.normalizeIdentifier(idOrName));
    }
}
