package dev.pokedex.web.dto;

import dev.pokedex.domain.model.User;

public record UserResponse(Long id, String username, String email, String role) {

    public static UserResponse from(User user) {
        return new UserResponse(user.id(), user.username(), user.email(), user.role().name());
    }
}
