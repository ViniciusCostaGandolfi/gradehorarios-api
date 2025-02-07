package br.com.gradehorarios.api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference; 
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.gradehorarios.api.application.dto.UserDto;
import br.com.gradehorarios.api.domain.entity.college.User;
import br.com.gradehorarios.api.infra.security.dtos.TokenJwtDto;

@Service 
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Autowired
    ObjectMapper objectMapper;


    public TokenJwtDto generateToken(User user) throws JsonProcessingException {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            var expirationTime = this.expiration();
            var userDto = new UserDto(user);
            var json = objectMapper.writeValueAsString(userDto);
            Map<String, Object> map = objectMapper.readValue(json, new TypeReference<>() {});
            var tokenJwt = JWT.create()
            .withIssuer("API gradehorarios.com.br")
            .withClaim("user", map)
            .withExpiresAt(expirationTime)
            .sign(algorithm);
            
            return new TokenJwtDto(tokenJwt);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token jwt", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            var token =  JWT.require(algorithm)
                    .withIssuer("API gradehorarios.com.br")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();

            return token;
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    public UserDto getUser(String tokenJWT) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            var userClaim = JWT.require(algorithm)
            .withIssuer("API gradehorarios.com.br")
            .build()
            .verify(tokenJWT)
            .getClaim("user")
            .asMap();
    
        return objectMapper.convertValue(userClaim, UserDto.class);
    
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }


    private Instant expiration() {
        return LocalDateTime.now().plusDays(3).toInstant(ZoneOffset.of("-03:00"));
    }

    public String generatePasswordResetToken(String email) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                .withIssuer("API gradehorarios.com.br")
                .withSubject(email)
                .withExpiresAt(LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00"))) // Expira em 30min
                .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token de redefinição de senha", exception);
        }
    }
    

}
