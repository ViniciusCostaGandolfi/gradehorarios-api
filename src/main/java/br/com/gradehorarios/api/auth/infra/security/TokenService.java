package br.com.gradehorarios.api.auth.infra.security;

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

import br.com.gradehorarios.api.auth.domain.model.User;
import br.com.gradehorarios.api.auth.infra.security.dto.JwtResponse;
import br.com.gradehorarios.api.auth.infra.security.dto.JwtUserDto;


@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Autowired
    ObjectMapper objectMapper;


    public JwtResponse generateToken(User user) throws JsonProcessingException {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            var expirationTime = this.expiration();
            var userDto = new JwtUserDto(user);
            var json = objectMapper.writeValueAsString(userDto);
            Map<String, Object> map = objectMapper.readValue(json, new TypeReference<>() {});
            var tokenJwt = JWT.create()
            .withIssuer("API gradehorarios.com.br")
            .withClaim("user", map)
            .withExpiresAt(expirationTime)
            .sign(algorithm);
            
            return new JwtResponse(tokenJwt);
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

    public JwtUserDto getUserFromToken(String tokenJWT) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            var userClaim =  JWT.require(algorithm)
                    .withIssuer("API gradehorarios.com.br")
                    .build()
                    .verify(tokenJWT)
                    .getClaim("user")
                    .asMap();

            return objectMapper.convertValue(userClaim, JwtUserDto.class);
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    
    private Instant expiration() {
        return LocalDateTime.now().plusDays(14).toInstant(ZoneOffset.of("-03:00"));
    }
}