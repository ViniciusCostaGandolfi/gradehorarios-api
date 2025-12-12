package br.com.gradehorarios.gradehorarios.auth.application.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import br.com.gradehorarios.gradehorarios.auth.application.dto.LoginRequest;
import br.com.gradehorarios.gradehorarios.auth.application.dto.RegisterRequest;
import br.com.gradehorarios.gradehorarios.auth.application.dto.UpdateUserRequest;
import br.com.gradehorarios.gradehorarios.auth.application.dto.UserResponseDTO;
import br.com.gradehorarios.gradehorarios.auth.application.service.UserService;
import br.com.gradehorarios.gradehorarios.auth.domain.model.RoleName;
import br.com.gradehorarios.gradehorarios.auth.infra.security.dto.JwtResponse;
import br.com.gradehorarios.gradehorarios.auth.infra.security.dto.JwtUserDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest data) throws Exception {
        return ResponseEntity.ok(userService.login(data));
    }

    @PostMapping("/sigin")
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest data) throws Exception {
        return ResponseEntity.ok(userService.register(data));

    }
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> listAll(Authentication authentication) throws Exception {
        JwtUserDto user = (JwtUserDto) authentication.getPrincipal();

        if (user.role() != RoleName.ROLE_ADMIN) {
            throw new AccessDeniedException("Acesso negado: Apenas administradores podem listar usuários.");
        }

        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id, Authentication authentication) throws Exception {
        JwtUserDto user = (JwtUserDto) authentication.getPrincipal();

        if (user.role() != RoleName.ROLE_ADMIN && !user.id().equals(id)) {
            throw new AccessDeniedException("Acesso negado: Você só pode visualizar seu próprio perfil.");
        }

        return ResponseEntity.ok(userService.findById(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) throws Exception {
        JwtUserDto user = (JwtUserDto) authentication.getPrincipal();

        if (user.role() != RoleName.ROLE_ADMIN && !user.id().equals(id)) {
            throw new AccessDeniedException("Acesso negado: Você não tem permissão para excluir este usuário.");
        }

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}")
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
}