package br.com.gradehorarios.api.application.service;

import br.com.gradehorarios.api.application.dto.CollegeDto;
import br.com.gradehorarios.api.application.dto.UserDto;
import br.com.gradehorarios.api.domain.entity.college.College;
import br.com.gradehorarios.api.domain.entity.college.User;
import br.com.gradehorarios.api.domain.repository.CollegeRepository;
import br.com.gradehorarios.api.domain.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CollegeService {

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private UserRepository userRepository;


    public College getById(Integer id) {
        return collegeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("College não encontrado."));
    }

    public List<College> getAllByUserId(Integer id) {
        return collegeRepository.findAllByUserId(id);
    }

    @Transactional
    public College updateOrCreate(CollegeDto collegeDto, UserDto userDto) {
        College college = collegeDto.id() != null
                ? collegeRepository.findById(collegeDto.id()).orElse(new College())
                : new College();

        User user = this.userRepository.findById(userDto.id())
                            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrada"));

        

        college.setCode(collegeDto.code());
        college.setName(collegeDto.name());

        college.setUser(user);
        user.getColleges().add(college);

        return collegeRepository.save(college);
    }

    @Transactional
    public void deleteById(Integer id, UserDto userDto) {
        User user = this.userRepository.findById(userDto.id())
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrada"));

        College college = this.collegeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("College não encontrada"));
        
        college.setUser(null);
        user.getColleges().remove(college);

    }
}
