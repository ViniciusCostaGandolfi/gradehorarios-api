package br.com.gradehorarios.gradehorarios.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.gradehorarios.gradehorarios.institution.domain.model.Institution;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_institution_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserInstitutionRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institutionId")
    private Institution institution;

    @Enumerated(EnumType.STRING)
    private UserInstitutionRoleName role;
}