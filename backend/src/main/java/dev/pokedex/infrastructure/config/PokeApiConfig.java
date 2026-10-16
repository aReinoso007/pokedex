package dev.pokedex.infrastructure.config;

import dev.pokedex.domain.port.PokemonCatalogPort;
import dev.pokedex.infrastructure.pokeapi.PokeApiCatalogAdapter;
import dev.pokedex.infrastructure.pokeapi.PokeApiClient;
import dev.pokedex.infrastructure.pokeapi.PokeApiProperties;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableCaching
@EnableConfigurationProperties(PokeApiProperties.class)
public class PokeApiConfig {

    @Bean
    PokeApiClient pokeApiClient(RestClient.Builder builder, PokeApiProperties props) {
        var settings = ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(props.timeoutSeconds()))
                .withReadTimeout(Duration.ofSeconds(props.timeoutSeconds()));
        RestClient rest = builder
                .baseUrl(props.baseUrl())
                .defaultHeader("Accept", "application/json")
                .requestFactory(ClientHttpRequestFactories.get(settings))
                .build();
        return new PokeApiClient(rest);
    }

    @Bean(destroyMethod = "shutdown")
    ExecutorService pokeApiExecutor() {
        return Executors.newFixedThreadPool(8);
    }

    @Bean
    PokemonCatalogPort pokemonCatalogPort(PokeApiClient client, ExecutorService pokeApiExecutor) {
        return new PokeApiCatalogAdapter(client, pokeApiExecutor);
    }
}
