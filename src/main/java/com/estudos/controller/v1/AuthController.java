package com.estudos.controller.v1;

import com.estudos.data.dto.security.AuthenticationRequest;
import com.estudos.data.dto.security.AuthenticationResponse;
import com.estudos.data.dto.security.RegisterRequest;
import com.estudos.data.dto.user.UserInfoDTO;
import com.estudos.repository.UserRepository;
import com.estudos.services.v1.AuthenticationServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth/v1")
@Tag(name = "Authentication Endpoint")
@Slf4j
public class AuthController {

    @Autowired
    private UserRepository repository;
    @Autowired
    private AuthenticationServices service;

    @Operation(summary = "Create account in the application")
    @ApiResponse(responseCode = "201", description = "Created", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterRequest.class))
    })
    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Adicionando um novo usuário");

        AuthenticationResponse newUser = service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);

    }

    @Operation(summary = "Login to the system")
    @ApiResponse(responseCode = "200", description = "Success", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationRequest.class))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authentication(@Valid @RequestBody AuthenticationRequest request) {
        log.info("Entering the system");

        return ResponseEntity.ok(service.authenticate(request));
    }

    @Operation(summary = "Get information from the logged in user")
    @ApiResponse(responseCode = "200", description = "Success", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserInfoDTO.class))
    })
    @GetMapping
    public ResponseEntity<Optional<UserInfoDTO>> infoUser() {
        log.info("Fetching logged in user data");

        return ResponseEntity.ok(service.infoUser());
    }
}
