package dev.pokedex.support;

import dev.pokedex.domain.model.User;
import dev.pokedex.domain.port.PasswordHasher;
import dev.pokedex.domain.port.TokenIssuer;
import java.time.Instant;

public final class FakeSecurityPorts {

    private FakeSecurityPorts() {
    }

    public static PasswordHasher hasher() {
        return new PasswordHasher() {
            @Override
            public String hash(String rawPassword) {
                return "hash:" + rawPassword;
            }

            @Override
            public boolean matches(String rawPassword, String hash) {
                return hash(rawPassword).equals(hash);
            }
        };
    }

    public static TokenIssuer tokenIssuer() {
        return (User user) -> new TokenIssuer.IssuedToken("token-for-" + user.username(), Instant.parse("2030-01-01T00:00:00Z"));
    }
}
