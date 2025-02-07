package br.com.gradehorarios.api.application.service;

import br.com.gradehorarios.api.application.dto.FullClassroomDto;
import br.com.gradehorarios.api.application.dto.TeacherDisciplineClassroomDto;
import br.com.gradehorarios.api.domain.entity.college.Classroom;
import br.com.gradehorarios.api.domain.entity.college.ClassroomDailySchedule;
import br.com.gradehorarios.api.domain.entity.college.College;
import br.com.gradehorarios.api.domain.entity.college.Discipline;
import br.com.gradehorarios.api.domain.entity.college.Teacher;
import br.com.gradehorarios.api.domain.entity.college.TeacherDisciplineClassroom;
import br.com.gradehorarios.api.domain.repository.ClassroomDailyScheduleRepository;
import br.com.gradehorarios.api.domain.repository.ClassroomRepository;
import br.com.gradehorarios.api.domain.repository.CollegeRepository;
import br.com.gradehorarios.api.domain.repository.DisciplineRepository;
import br.com.gradehorarios.api.domain.repository.TeacherDisciplineClassroomRepository;
import br.com.gradehorarios.api.domain.repository.TeacherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ClassroomDailyScheduleRepository classroomDailyScheduleRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherDisciplineClassroomRepository teacherDisciplineClassroomRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;


    public List<Classroom> getAllByCollegeId(Integer CollegeId) {
        return classroomRepository.findAllByCollegeId(CollegeId);
    }

    public Classroom getById(Integer id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom não encontrada."));
    }

    @Transactional
    public Classroom updateOrCreate(FullClassroomDto classroomDto, Integer collegeId, Integer userId) {

        Classroom classroom = classroomDto.id() != null
                ? classroomRepository.findById(classroomDto.id()).orElse(new Classroom())
                : new Classroom();

        College college = this.collegeRepository.findByIdAndUserId(collegeId, userId).orElseThrow(
            () -> new Error("Escola não encontrada.")
        );

        classroom.setName(classroomDto.name());

        college.addClassroom(classroom);

        if (classroomDto.classroomDailySchedule() != null) {
            ClassroomDailySchedule schedule = classroomDto.classroomDailySchedule().id() != null
                ? classroomDailyScheduleRepository.findById(classroomDto.classroomDailySchedule().id()).orElse(new ClassroomDailySchedule())
                : new ClassroomDailySchedule();

            schedule.setMondayClasses(classroomDto.classroomDailySchedule().mondayClasses());
            schedule.setTuesdayClasses(classroomDto.classroomDailySchedule().tuesdayClasses());
            schedule.setWednesdayClasses(classroomDto.classroomDailySchedule().wednesdayClasses());
            schedule.setThursdayClasses(classroomDto.classroomDailySchedule().thursdayClasses());
            schedule.setFridayClasses(classroomDto.classroomDailySchedule().fridayClasses());
            schedule.setSaturdayClasses(classroomDto.classroomDailySchedule().saturdayClasses());
            schedule.setSundayClasses(classroomDto.classroomDailySchedule().sundayClasses());
            schedule.setClassroom(classroom);
            classroom.setClassroomDailySchedule(schedule);
        }

         if (classroomDto.teacherDisciplineClassrooms() != null) {
            List<Integer> dtoDisciplineClassroomIds = classroomDto.teacherDisciplineClassrooms().stream()
                    .map(TeacherDisciplineClassroomDto::id)
                    .filter(id -> id != null)
                    .collect(Collectors.toList());

            List<TeacherDisciplineClassroom> classroomsToRemove = classroom.getTeacherDisciplineClassrooms().stream()
                    .filter(disciplineClassroom -> !dtoDisciplineClassroomIds.contains(disciplineClassroom.getId()))
                    .collect(Collectors.toList());

            classroom.getTeacherDisciplineClassrooms().removeAll(classroomsToRemove);

            List<TeacherDisciplineClassroom> newOrUpdatedClassrooms = classroomDto.teacherDisciplineClassrooms().stream()
                    .map(dto -> {
                        TeacherDisciplineClassroom disciplineClassroom = dto.id() != null
                                ? teacherDisciplineClassroomRepository.findById(dto.id()).orElse(new TeacherDisciplineClassroom())
                                : new TeacherDisciplineClassroom();

                        Discipline discipline = disciplineRepository.findById(dto.disciplineId())
                                .orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada"));

                        Teacher teacher = teacherRepository.findById(dto.teacherId())
                                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));

                        disciplineClassroom.setTeacher(teacher);
                        disciplineClassroom.setDiscipline(discipline);
                        disciplineClassroom.setClassroom(classroom);
                        disciplineClassroom.setTotalClasses(dto.totalClasses());
                        return disciplineClassroom;
                    })
                    .collect(Collectors.toList());

                classroom.getTeacherDisciplineClassrooms().addAll(newOrUpdatedClassrooms);
        }

        return classroomRepository.save(classroom);
    }


    


    @Transactional
    public void deleteById(Integer id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom não encontrada."));

        classroomRepository.delete(classroom);
    }


}
