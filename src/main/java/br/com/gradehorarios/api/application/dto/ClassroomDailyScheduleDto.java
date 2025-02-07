package br.com.gradehorarios.api.application.dto;

import br.com.gradehorarios.api.domain.entity.college.ClassroomDailySchedule;

public record ClassroomDailyScheduleDto(
    Integer id,
    Integer mondayClasses,
    Integer tuesdayClasses,
    Integer wednesdayClasses,
    Integer thursdayClasses,
    Integer fridayClasses,
    Integer saturdayClasses,
    Integer sundayClasses
) {

    public ClassroomDailyScheduleDto(ClassroomDailySchedule schedule) {
        this(
            schedule.getId(),
            schedule.getMondayClasses(),
            schedule.getTuesdayClasses(),
            schedule.getWednesdayClasses(),
            schedule.getThursdayClasses(),
            schedule.getFridayClasses(),
            schedule.getSaturdayClasses(),
            schedule.getSundayClasses()
        );
    }
}
