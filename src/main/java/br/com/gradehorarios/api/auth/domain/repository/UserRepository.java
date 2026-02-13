package br.com.gradehorarios.api.auth.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gradehorarios.api.auth.domain.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean  existsByEmail(String email);
    Optional<User> findByEmail(String email);

}
