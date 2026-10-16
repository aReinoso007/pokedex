package dev.pokedex.domain.port;

import dev.pokedex.domain.model.PageResult;
import dev.pokedex.domain.model.Pokemon;
import java.util.Optional;

/** Outbound port to the local Pokemon store. */
public interface PokemonRepository {

    PageResult<Pokemon> findAll(int page, int size);

    Optional<Pokemon> findById(int id);

    boolean existsById(int id);

    Pokemon save(Pokemon pokemon);

    /** @return {@code true} if a record was deleted */
    boolean deleteById(int id);
}
