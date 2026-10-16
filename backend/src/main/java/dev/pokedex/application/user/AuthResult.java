package dev.pokedex.application.user;

import dev.pokedex.domain.model.User;
import java.time.Instant;

public record AuthResult(String token, Instant expiresAt, User user) {
}
