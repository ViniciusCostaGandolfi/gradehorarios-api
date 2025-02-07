package br.com.gradehorarios.api.domain.repository;

import br.com.gradehorarios.api.domain.entity.college.StateRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StateRegistrationRepository extends JpaRepository<StateRegistration, Integer> {

    Optional<StateRegistration> findByCode(Integer code);
}
