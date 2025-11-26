package br.com.gradehorarios.gradehorarios.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gradehorarios.gradehorarios.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean  existsByEmail(String email);
}
