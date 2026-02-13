package br.com.gradehorarios.api.auth.application.service;


import java.util.Collections;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import br.com.gradehorarios.api.auth.application.dto.LoginRequest;
import br.com.gradehorarios.api.auth.application.dto.RegisterRequest;
import br.com.gradehorarios.api.auth.application.dto.UpdateUserRequest;
import br.com.gradehorarios.api.auth.application.dto.UserResponseDTO;
import br.com.gradehorarios.api.auth.domain.model.RoleName;
import br.com.gradehorarios.api.auth.domain.model.User;
import br.com.gradehorarios.api.auth.domain.repository.UserRepository;
import br.com.gradehorarios.api.auth.infra.security.TokenService;
import br.com.gradehorarios.api.auth.infra.security.dto.JwtResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${google.api.client}") 
    private String googleClientId;


    @Transactional
    public JwtResponse register(RegisterRequest data) throws Exception {
        if (userRepository.existsByEmail(data.email())) {
            throw new IllegalArgumentException("Email já cadastrado no sistema.");
        }

        User newUser = new User();
        newUser.setEmail(data.email());
        newUser.setPassword(passwordEncoder.encode(data.password()));
        newUser.setActive(true);
        newUser.setName(data.name());
        
        newUser.setRole(RoleName.ROLE_USER);
        
        userRepository.save(newUser);

        return tokenService.generateToken(newUser);
    }


    public JwtResponse login(LoginRequest data) throws JsonProcessingException {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        var auth = authenticationManager.authenticate(usernamePassword);

        var user = (User) auth.getPrincipal();

        return tokenService.generateToken(user);
    }


    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private UserResponseDTO mapToDto(User user) {
        return new UserResponseDTO(user);
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        return new UserResponseDTO(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado para exclusão.");
        }
        userRepository.deleteById(id);
    }
    
    @Transactional
    public UserResponseDTO updateUser(Long id, UpdateUserRequest data) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));


        if (data.role() != null) {
            user.setRole(data.role());
        }

        userRepository.save(user);

        return new UserResponseDTO(user);
    }



    @Transactional
    public JwtResponse loginWithGoogle(String googleToken) throws Exception {
        
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(this.googleClientId))
                .build();
        GoogleIdToken idToken = verifier.verify(googleToken);
        if (idToken == null) {
            throw new IllegalArgumentException("Token do Google inválido.");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); 
            newUser.setRole(RoleName.ROLE_USER);
            newUser.setActive(true);
            return userRepository.save(newUser);
        });

        return tokenService.generateToken(user);
    }


    

}
