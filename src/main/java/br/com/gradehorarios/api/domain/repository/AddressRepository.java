package br.com.gradehorarios.api.domain.repository;

import br.com.gradehorarios.api.domain.entity.college.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
