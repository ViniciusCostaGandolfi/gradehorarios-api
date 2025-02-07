package br.com.gradehorarios.api.domain.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gradehorarios.api.domain.entity.college.User;


public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);


}