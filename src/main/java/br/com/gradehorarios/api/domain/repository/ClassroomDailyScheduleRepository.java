package br.com.gradehorarios.api.domain.repository;

import br.com.gradehorarios.api.domain.entity.college.ClassroomDailySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomDailyScheduleRepository extends JpaRepository<ClassroomDailySchedule, Integer> {
}
