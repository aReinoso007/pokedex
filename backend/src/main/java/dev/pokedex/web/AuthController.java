package dev.pokedex.web;

import dev.pokedex.application.user.AuthenticateUser;
import dev.pokedex.application.user.RegisterUser;
import dev.pokedex.web.dto.LoginRequest;
import dev.pokedex.web.dto.LoginResponse;
import dev.pokedex.web.dto.RegisterRequest;
import dev.pokedex.web.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Registration and login (public)")
public class AuthController {

    private final RegisterUser registerUser;
    private final AuthenticateUser authenticateUser;

    public AuthController(RegisterUser registerUser, AuthenticateUser authenticateUser) {
        this.registerUser = registerUser;
        this.authenticateUser = authenticateUser;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user")
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return UserResponse.from(registerUser.execute(request.username(), request.email(), request.password()));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate and receive a JWT")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return LoginResponse.from(authenticateUser.execute(request.username(), request.password()));
    }
}
