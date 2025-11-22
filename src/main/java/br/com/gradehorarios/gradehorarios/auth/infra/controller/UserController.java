package br.com.gradehorarios.gradehorarios.auth.infra.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.gradehorarios.gradehorarios.auth.application.dto.LoginRequest;
import br.com.gradehorarios.gradehorarios.auth.application.dto.RegisterRequest;
import br.com.gradehorarios.gradehorarios.auth.application.dto.UpdateUserRequest;
import br.com.gradehorarios.gradehorarios.auth.application.dto.UserResponseDTO;
import br.com.gradehorarios.gradehorarios.auth.application.service.UserService;
import br.com.gradehorarios.gradehorarios.bootstrap.dto.JwtResponse;

import java.util.List;

@RestController
@RequestMapping("/api/auth/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest data) throws Exception {
        return ResponseEntity.ok(userService.login(data));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest data) {
        userService.register(data);
        return ResponseEntity.status(201).build();
    }


    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listAll() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody UpdateUserRequest data) {
        return ResponseEntity.ok(userService.updateUser(id, data));
    }
}