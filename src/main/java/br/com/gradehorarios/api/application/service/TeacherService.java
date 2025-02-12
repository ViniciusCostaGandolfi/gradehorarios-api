package br.com.gradehorarios.api.application.service;

import br.com.gradehorarios.api.application.dto.FullTeacherDto;
import br.com.gradehorarios.api.domain.entity.college.College;
import br.com.gradehorarios.api.domain.entity.college.Teacher;
import br.com.gradehorarios.api.domain.entity.college.TeacherAvailability;
import br.com.gradehorarios.api.domain.entity.college.TeacherClassroomDiscipline;
import br.com.gradehorarios.api.domain.repository.CollegeRepository;
import br.com.gradehorarios.api.domain.repository.TeacherAvailabilityRepository;
import br.com.gradehorarios.api.domain.repository.TeacherClassroomDisciplineRepository;
import br.com.gradehorarios.api.domain.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;


    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private TeacherClassroomDisciplineRepository teacherClassroomDisciplineRepository;

    @Autowired
    private TeacherAvailabilityRepository teacherAvailabilityRepository;

    public List<Teacher> getAllByCollegeId(Integer collegeId) {
        return teacherRepository.findAllByCollegeId(collegeId);
    }

    public Teacher getById(Integer id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher não encontrado."));
    }

    @Transactional
    public Teacher updateOrCreate(FullTeacherDto teacherDto, Integer collegeId, Integer userId) {
        Teacher teacher = teacherDto.id() != null
                ? teacherRepository.findById(teacherDto.id()).orElse(new Teacher())
                : new Teacher();

        College college = this.collegeRepository.findByIdAndUserId(collegeId, userId).orElseThrow(
                () -> new EntityNotFoundException("Escola não encontrada.")
        );
        
        teacher.setCollege(college);
        teacher.setName(teacherDto.name());
        teacher.setPreferDoubleClass(teacherDto.preferDoubleClass());
        teacher.setPreferFirstClass(teacherDto.preferFirstClass());
        teacher.setPreferLastClass(teacherDto.preferLastClass());


        if (teacherDto.teacherAvailability() != null) {

                TeacherAvailability teacherAvailability = teacherDto.teacherAvailability().id() != null ?
                        this.teacherAvailabilityRepository.findById(teacherDto.teacherAvailability().id()).orElse( new TeacherAvailability())
                : new TeacherAvailability();

                teacherAvailability.setMonday(teacherDto.teacherAvailability().monday());
                teacherAvailability.setTuesday(teacherDto.teacherAvailability().tuesday());
                teacherAvailability.setWednesday(teacherDto.teacherAvailability().wednesday());
                teacherAvailability.setThursday(teacherDto.teacherAvailability().thursday());
                teacherAvailability.setFriday(teacherDto.teacherAvailability().friday());
                teacherAvailability.setSaturday(teacherDto.teacherAvailability().saturday());
                teacherAvailability.setSunday(teacherDto.teacherAvailability().sunday());

                teacherAvailability.setTeacher(teacher);
                teacher.setTeacherAvailability(teacherAvailability);
        }


        return teacherRepository.save(teacher);
    }


        @Transactional
        public void deleteById(Integer id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher não encontrado."));

        List<TeacherClassroomDiscipline> disciplines = teacherClassroomDisciplineRepository.findAllByTeacherId(id);

        for (TeacherClassroomDiscipline discipline : disciplines) {
                discipline.setTeacher(null);
        }

        teacherRepository.delete(teacher);
        }


}
