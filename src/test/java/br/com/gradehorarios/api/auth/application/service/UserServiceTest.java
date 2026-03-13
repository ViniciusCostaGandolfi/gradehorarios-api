package br.com.gradehorarios.api.auth.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

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

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private OAuthProviderService mockProviderService;

    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        when(mockProviderService.getProviderName()).thenReturn("google");
        userService = new UserService(userRepository, passwordEncoder, tokenService, authenticationManager, List.of(mockProviderService));
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setName("John Doe");
        sampleUser.setEmail("john@example.com");
        sampleUser.setPassword("encodedPassword");
        sampleUser.setRole(RoleName.ROLE_USER);
        sampleUser.setActive(true);
    }

    @Test
    void register_Success() throws Exception {
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "password123");
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(tokenService.generateToken(any(User.class))).thenReturn(new JwtResponse("dummyToken"));

        JwtResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals("dummyToken", response.token());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_EmailAlreadyExists_ThrowsException() {
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "password123");
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() throws Exception {
        LoginRequest request = new LoginRequest("john@example.com", "password123");
        Authentication authentication = mock(Authentication.class);
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(sampleUser);
        when(tokenService.generateToken(sampleUser)).thenReturn(new JwtResponse("dummyToken"));

        JwtResponse response = userService.login(request);

        assertNotNull(response);
        assertEquals("dummyToken", response.token());
    }

    @Test
    void findAllUsers_ReturnsList() {
        when(userRepository.findAll()).thenReturn(List.of(sampleUser));

        List<UserResponseDTO> result = userService.findAllUsers();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).name());
    }

    @Test
    void findById_UserExists_ReturnsDto() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        UserResponseDTO result = userService.findById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.name());
    }

    @Test
    void findById_UserDoesNotExist_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findById(1L));
    }

    @Test
    void deleteUser_UserExists_DeletesUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_UserDoesNotExist_ThrowsException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateUser_UserExists_UpdatesRoleAndReturnsDto() {
        UpdateUserRequest request = new UpdateUserRequest("new name", RoleName.ROLE_ADMIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        UserResponseDTO result = userService.updateUser(1L, request);

        assertNotNull(result);
        assertEquals(RoleName.ROLE_ADMIN, sampleUser.getRole());
        verify(userRepository).save(sampleUser);
    }

    @Test
    void oauthLogin_ExistingUser_GeneratesToken() throws Exception {
        String token = "valid_token";
        OAuthUserInfo oauthInfo = new OAuthUserInfo("john@example.com", "John Doe");

        when(mockProviderService.getUserInfo(token)).thenReturn(oauthInfo);
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(sampleUser));
        when(tokenService.generateToken(sampleUser)).thenReturn(new JwtResponse("oauthToken"));

        JwtResponse response = userService.oauthLogin("google", token);

        assertNotNull(response);
        assertEquals("oauthToken", response.token());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void oauthLogin_NewUser_CreatesUserAndGeneratesToken() throws Exception {
        String token = "valid_token";
        OAuthUserInfo oauthInfo = new OAuthUserInfo("newuser@example.com", "New User");
        
        User newUser = new User();
        newUser.setEmail("newuser@example.com");

        when(mockProviderService.getUserInfo(token)).thenReturn(oauthInfo);
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedUUID");
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(tokenService.generateToken(newUser)).thenReturn(new JwtResponse("newOauthToken"));

        JwtResponse response = userService.oauthLogin("google", token);

        assertNotNull(response);
        assertEquals("newOauthToken", response.token());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void oauthLogin_UnsupportedProvider_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> userService.oauthLogin("unsupported", "token"));
    }
}
