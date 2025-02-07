package br.com.gradehorarios.api.application.dto;

import br.com.gradehorarios.api.domain.entity.college.Preference;
import br.com.gradehorarios.api.domain.entity.college.TeacherAvailability;

public record TeacherAvailabilityDto(
    Integer id,
    Preference monday,
    Preference tuesday,
    Preference wednesday,
    Preference thursday,
    Preference friday,
    Preference saturday,
    Preference sunday
) {

    public TeacherAvailabilityDto(TeacherAvailability availability) {
        this(
            availability.getId(),
            availability.getMonday(),
            availability.getTuesday(),
            availability.getWednesday(),
            availability.getThursday(),
            availability.getFriday(),
            availability.getSaturday(),
            availability.getSunday()
        );
    }
}
