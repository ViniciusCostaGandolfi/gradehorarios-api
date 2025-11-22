package br.com.gradehorarios.gradehorarios.auth.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gradehorarios.gradehorarios.auth.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean  existsByEmail(String email);
}
