package br.com.gradehorarios.gradehorarios.auth.application.service;


import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.gradehorarios.gradehorarios.auth.application.dto.LoginRequest;
import br.com.gradehorarios.gradehorarios.auth.application.dto.RegisterRequest;
import br.com.gradehorarios.gradehorarios.auth.application.dto.UpdateUserRequest;
import br.com.gradehorarios.gradehorarios.auth.application.dto.UserResponseDTO;
import br.com.gradehorarios.gradehorarios.auth.domain.entity.RoleName;
import br.com.gradehorarios.gradehorarios.auth.domain.entity.User;
import br.com.gradehorarios.gradehorarios.auth.infra.repository.UserRepository;
import br.com.gradehorarios.gradehorarios.bootstrap.dto.JwtResponse;
import br.com.gradehorarios.gradehorarios.bootstrap.security.TokenService;

import java.util.List;
import java.util.stream.Collectors;

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


    @Transactional
    public void register(RegisterRequest data) {
        if (userRepository.existsByEmail(data.email())) {
            throw new IllegalArgumentException("Email já cadastrado no sistema.");
        }

        User newUser = new User();
        newUser.setEmail(data.email());
        newUser.setPassword(passwordEncoder.encode(data.password()));
        
        newUser.setRole(RoleName.ROLE_USER);
        
        
        userRepository.save(newUser);
    }

    public JwtResponse login(LoginRequest data) throws Exception {
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

}
