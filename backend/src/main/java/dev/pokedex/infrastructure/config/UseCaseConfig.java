package dev.pokedex.infrastructure.config;

import dev.pokedex.application.pokemon.DeleteLocalPokemon;
import dev.pokedex.application.pokemon.GetLocalPokemon;
import dev.pokedex.application.pokemon.GetPokemonDetail;
import dev.pokedex.application.pokemon.ListLocalPokemon;
import dev.pokedex.application.pokemon.ListPokemon;
import dev.pokedex.application.pokemon.ListTags;
import dev.pokedex.application.pokemon.SearchPokemon;
import dev.pokedex.application.pokemon.SyncPokemon;
import dev.pokedex.application.pokemon.UpdateLocalPokemon;
import dev.pokedex.application.user.AuthenticateUser;
import dev.pokedex.application.user.RegisterUser;
import dev.pokedex.domain.port.PasswordHasher;
import dev.pokedex.domain.port.PokemonCatalogPort;
import dev.pokedex.domain.port.PokemonRepository;
import dev.pokedex.domain.port.TagRepository;
import dev.pokedex.domain.port.TokenIssuer;
import dev.pokedex.domain.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Composition root: the application layer is plain Java, Spring only wires it here. */
@Configuration
public class UseCaseConfig {

    @Bean
    ListPokemon listPokemon(PokemonCatalogPort catalog) {
        return new ListPokemon(catalog);
    }

    @Bean
    SearchPokemon searchPokemon(PokemonCatalogPort catalog) {
        return new SearchPokemon(catalog);
    }

    @Bean
    GetPokemonDetail getPokemonDetail(PokemonCatalogPort catalog) {
        return new GetPokemonDetail(catalog);
    }

    @Bean
    SyncPokemon syncPokemon(PokemonCatalogPort catalog, PokemonRepository repository) {
        return new SyncPokemon(catalog, repository);
    }

    @Bean
    ListLocalPokemon listLocalPokemon(PokemonRepository repository) {
        return new ListLocalPokemon(repository);
    }

    @Bean
    GetLocalPokemon getLocalPokemon(PokemonRepository repository) {
        return new GetLocalPokemon(repository);
    }

    @Bean
    UpdateLocalPokemon updateLocalPokemon(PokemonRepository repository, TagRepository tags) {
        return new UpdateLocalPokemon(repository, tags);
    }

    @Bean
    ListTags listTags(TagRepository tags) {
        return new ListTags(tags);
    }

    @Bean
    DeleteLocalPokemon deleteLocalPokemon(PokemonRepository repository) {
        return new DeleteLocalPokemon(repository);
    }

    @Bean
    RegisterUser registerUser(UserRepository users, PasswordHasher hasher) {
        return new RegisterUser(users, hasher);
    }

    @Bean
    AuthenticateUser authenticateUser(UserRepository users, PasswordHasher hasher, TokenIssuer tokens) {
        return new AuthenticateUser(users, hasher, tokens);
    }
}
