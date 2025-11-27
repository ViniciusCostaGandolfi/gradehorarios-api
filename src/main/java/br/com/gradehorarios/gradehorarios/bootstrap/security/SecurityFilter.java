package br.com.gradehorarios.gradehorarios.bootstrap.security;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.gradehorarios.gradehorarios.bootstrap.security.dto.JwtUserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;



    private String recoverToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "").trim();
        }
        return null;
    }



    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var tokenJwt = recoverToken(request);

        if (tokenJwt != null) {
            try {
                JwtUserDto userDto = tokenService.getUserFromToken(tokenJwt);

                var authorities = new ArrayList<SimpleGrantedAuthority>();
                
                authorities.add(new SimpleGrantedAuthority(userDto.role().toString()));
                if (userDto.institutions() != null) {
                    userDto.institutions().forEach(inst -> {
                        String authName = String.format("INSTITUTION_%d_%s", 
                            inst.institutionId(), 
                            inst.role()
                        );
                        authorities.add(new SimpleGrantedAuthority(authName));
                    });
                }
                var authentication = new UsernamePasswordAuthenticationToken(
                        userDto,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                
            } catch (Exception e) {
                System.out.println("Erro ao processar token: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}