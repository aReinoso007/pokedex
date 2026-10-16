package dev.pokedex.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataPokemonRepository extends JpaRepository<PokemonEntity, Integer> {
}
