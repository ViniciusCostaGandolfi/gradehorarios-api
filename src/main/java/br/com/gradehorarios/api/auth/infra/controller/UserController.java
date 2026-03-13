package br.com.gradehorarios.api.auth.infra.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import br.com.gradehorarios.api.auth.application.dto.LoginRequest;
import br.com.gradehorarios.api.auth.application.dto.OAuthLoginRequestDto;
import br.com.gradehorarios.api.auth.application.dto.RegisterRequest;
import br.com.gradehorarios.api.auth.application.dto.UpdateUserRequest;
import br.com.gradehorarios.api.auth.application.dto.UserResponseDTO;
import br.com.gradehorarios.api.auth.application.service.UserService;
import br.com.gradehorarios.api.auth.domain.model.RoleName;
import br.com.gradehorarios.api.auth.infra.security.dto.JwtResponse;
import br.com.gradehorarios.api.auth.infra.security.dto.JwtUserDto;

import org.springframework.security.core.Authentication;

import java.nio.file.AccessDeniedException;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "User API", description = "Endpoints for user authentication and management")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT token")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest data) throws Exception {
        return ResponseEntity.ok(userService.login(data));
    }

    @PostMapping("/sigin")
    @Operation(summary = "Register user", description = "Registers a new user in the system")
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest data) throws Exception {
        return ResponseEntity.ok(userService.register(data));

    }
    @GetMapping("/users")
    @Operation(summary = "List all users", description = "Returns a list of all registered users (Admin only)")
    public ResponseEntity<List<UserResponseDTO>> listAll(Authentication authentication) throws Exception {
        JwtUserDto user = (JwtUserDto) authentication.getPrincipal();

        if (user.role() != RoleName.ROLE_ADMIN) {
            throw new AccessDeniedException("Acesso negado: Apenas administradores podem listar usuários.");
        }

        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get user by ID", description = "Returns user details by ID")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id, Authentication authentication) throws Exception {
        JwtUserDto user = (JwtUserDto) authentication.getPrincipal();

        if (user.role() != RoleName.ROLE_ADMIN && !user.id().equals(id)) {
            throw new AccessDeniedException("Acesso negado: Você só pode visualizar seu próprio perfil.");
        }

        return ResponseEntity.ok(userService.findById(id));
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) throws Exception {
        JwtUserDto user = (JwtUserDto) authentication.getPrincipal();

        if (user.role() != RoleName.ROLE_ADMIN && !user.id().equals(id)) {
            throw new AccessDeniedException("Acesso negado: Você não tem permissão para excluir este usuário.");
        }

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Update user", description = "Updates user details by ID")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody UpdateUserRequest data, Authentication authentication) throws Exception {
        JwtUserDto user = (JwtUserDto) authentication.getPrincipal();

        if (user.role() != RoleName.ROLE_ADMIN && !user.id().equals(id)) {
            throw new AccessDeniedException("Acesso negado.");
        }

        if (data.role() != null && user.role() != RoleName.ROLE_ADMIN) {
            throw new AccessDeniedException("Acesso negado: Apenas administradores podem alterar cargos.");
        }

        return ResponseEntity.ok(userService.updateUser(id, data));
    }

    @PostMapping("/oauth")
    @Operation(summary = "OAuth Login", description = "Authenticates a user using an external OAuth token")
    public ResponseEntity<JwtResponse> oauthLogin(@RequestBody OAuthLoginRequestDto request) throws Exception {
        return ResponseEntity.ok(userService.oauthLogin(request.provider(), request.token()));
    }
}
