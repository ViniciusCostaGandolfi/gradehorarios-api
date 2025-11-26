package br.com.gradehorarios.gradehorarios.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_institution_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "institution"})
@EqualsAndHashCode(of = "id")
public class UserInstitutionRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioId")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instituicaoId")
    private Institution institution;

    @Enumerated(EnumType.STRING)
    private UserInstitutionRoleName role;
}