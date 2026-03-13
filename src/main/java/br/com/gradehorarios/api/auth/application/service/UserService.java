package br.com.gradehorarios.api.auth.application.service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.gradehorarios.api.auth.domain.service.OAuthProviderService;
import br.com.gradehorarios.api.auth.domain.service.OAuthUserInfo;

import br.com.gradehorarios.api.auth.application.dto.LoginRequest;
import br.com.gradehorarios.api.auth.application.dto.RegisterRequest;
import br.com.gradehorarios.api.auth.application.dto.UpdateUserRequest;
import br.com.gradehorarios.api.auth.application.dto.UserResponseDTO;
import br.com.gradehorarios.api.auth.domain.model.RoleName;
import br.com.gradehorarios.api.auth.domain.model.User;
import br.com.gradehorarios.api.auth.domain.repository.UserRepository;
import br.com.gradehorarios.api.auth.infra.security.TokenService;
import br.com.gradehorarios.api.auth.infra.security.dto.JwtResponse;

import java.util.Map;
import java.util.function.Function;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final Map<String, OAuthProviderService> oauthProviders;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService,
            AuthenticationManager authenticationManager,
            List<OAuthProviderService> providersList
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.oauthProviders = providersList.stream()
                .collect(Collectors.toMap(OAuthProviderService::getProviderName, Function.identity()));
    }

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
    public JwtResponse oauthLogin(String providerName, String providerToken) throws Exception {
        OAuthProviderService providerService = oauthProviders.get(providerName.toLowerCase());
        
        if (providerService == null) {
            throw new IllegalArgumentException("Provedor OAuth não suportado: " + providerName);
        }

        OAuthUserInfo userInfo = providerService.getUserInfo(providerToken);

        User user = userRepository.findByEmail(userInfo.email()).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(userInfo.email());
            newUser.setName(userInfo.name());
            newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); 
            newUser.setRole(RoleName.ROLE_USER);
            newUser.setActive(true);
            return userRepository.save(newUser);
        });

        return tokenService.generateToken(user);
    }


    

}
