package br.com.gradehorarios.api.application.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.gradehorarios.api.application.dto.UserCreationDto;
import br.com.gradehorarios.api.application.service.PasswordResetService;
import br.com.gradehorarios.api.application.service.UserService;
import br.com.gradehorarios.api.domain.entity.college.User;
import br.com.gradehorarios.api.infra.security.TokenService;
import br.com.gradehorarios.api.infra.security.dtos.LoginDto;
import br.com.gradehorarios.api.infra.security.dtos.PasswordResetDto;
import br.com.gradehorarios.api.infra.security.dtos.PasswordResetRequestDto;
import br.com.gradehorarios.api.infra.security.dtos.TokenJwtDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping(ApiVersion.VERSION + "/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private TokenService tokenService;


    @PostMapping("/sigin") 
    @Transactional
    public ResponseEntity<TokenJwtDto> createMerchant(@RequestBody @Valid UserCreationDto userCreationDto) throws JsonProcessingException, IllegalArgumentException {
        User merchantUser = userService.createUser(userCreationDto);
        var tokenJwtDto = tokenService.generateToken(merchantUser);
        return ResponseEntity.ok().body(tokenJwtDto);
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<TokenJwtDto> login(@RequestBody @Valid LoginDto loginDto) throws JsonProcessingException, IllegalArgumentException {
        System.err.println(loginDto);
        new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());

        var user = userService.getUserByEmail(loginDto.email());
        var tokenJwtDto = tokenService.generateToken(user);
        
        return ResponseEntity.ok().body(tokenJwtDto);
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<String> requestPasswordReset(@RequestBody @Valid PasswordResetRequestDto request) throws JsonProcessingException {
        passwordResetService.sendPasswordResetEmail(request.email());
        return ResponseEntity.ok("E-mail de redefinição enviado!");
    }


    @PostMapping("/reset_password")
    @Transactional
    public ResponseEntity<String> resetPassword(@RequestBody @Valid PasswordResetDto request) {
        passwordResetService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }

}
