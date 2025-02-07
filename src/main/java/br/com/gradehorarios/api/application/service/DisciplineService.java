package br.com.gradehorarios.api.application.service;

import br.com.gradehorarios.api.application.dto.DisciplineDto;
import br.com.gradehorarios.api.domain.entity.college.College;
import br.com.gradehorarios.api.domain.entity.college.Discipline;
import br.com.gradehorarios.api.domain.repository.CollegeRepository;
import br.com.gradehorarios.api.domain.repository.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class DisciplineService {

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private CollegeRepository collegeRepository;


    public List<Discipline> getAllByCollegeId(Integer collegeId) {
        return disciplineRepository.findAllByCollegeId(collegeId);
    }


    public Discipline getByIdAndCollegeId(Integer disciplineId, Integer collegeId) {
        return disciplineRepository.findByIdAndCollegeId(disciplineId, collegeId)
                .orElseThrow(() -> new EntityNotFoundException("Discipline não encontrada para o college especificado."));
    }


    @Transactional
    public Discipline updateOrCreate(DisciplineDto disciplineDto, Integer collegeId, Integer userId) {
        College college = collegeRepository.findByIdAndUserId(collegeId, userId)
            .orElseThrow(() -> new EntityNotFoundException("Recurso não encontrado"));

        Discipline discipline = disciplineDto.id() != null
                ? disciplineRepository.findByIdAndCollegeId(disciplineDto.id(), college.getId())
                    .orElse(new Discipline())
                : new Discipline();

        discipline.setName(disciplineDto.name());

        discipline.setCode(disciplineDto.code());

        college.addDiscipline(discipline);

        return disciplineRepository.save(discipline);
    }

    @Transactional
    public void deleteById(Integer disciplineId, Integer collegeId, Integer userId) {
        College college = collegeRepository.findByIdAndUserId(collegeId, userId)
            .orElseThrow(() -> new EntityNotFoundException("Recurso não encontrado"));
        Discipline discipline = disciplineRepository.findByIdAndCollegeId(disciplineId, college.getId())
                .orElseThrow(() -> new EntityNotFoundException("Discipline não encontrada para o college especificado."));
        disciplineRepository.delete(discipline);
    }
}
