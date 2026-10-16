package dev.pokedex.domain.port;

import dev.pokedex.domain.model.PageResult;
import dev.pokedex.domain.model.Pokemon;
import dev.pokedex.domain.model.PokemonName;
import dev.pokedex.domain.model.PokemonSummary;
import java.util.List;

/** Outbound port to the remote Pokemon catalog (PokeAPI). */
public interface PokemonCatalogPort {

    /** @param page zero-based page index */
    PageResult<PokemonSummary> list(int page, int size);

    /** Every name in the catalog (id, name, sprite); lightweight and expected to be cached by the adapter. */
    List<PokemonName> listNames();

    /**
     * @param idOrName numeric id or lower-case name
     * @throws dev.pokedex.domain.exception.PokemonNotFoundException if it does not exist
     * @throws dev.pokedex.domain.exception.CatalogUnavailableException if the catalog fails
     */
    Pokemon fetch(String idOrName);
}
