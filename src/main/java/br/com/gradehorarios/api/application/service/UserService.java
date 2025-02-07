package br.com.gradehorarios.api.application.service;

import br.com.gradehorarios.api.application.dto.UserCreationDto;
import br.com.gradehorarios.api.domain.entity.college.User;
import br.com.gradehorarios.api.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User createUser(UserCreationDto userCreationDto) {
        User user = new User();
        user.setName(userCreationDto.name());
        user.setEmail(userCreationDto.email());
        user.setPassword(userCreationDto.password());
        user.setPhone(userCreationDto.phone());

        user = userRepository.save(user);

        return user;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }
}
