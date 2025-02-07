package br.com.gradehorarios.api.domain.repository;

import br.com.gradehorarios.api.domain.entity.college.TeacherAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherAvailabilityRepository extends JpaRepository<TeacherAvailability, Integer> {
}
