package dev.pokedex.infrastructure.security;

import dev.pokedex.domain.model.Role;

public record AuthenticatedUser(Long id, String username, Role role) {
}
