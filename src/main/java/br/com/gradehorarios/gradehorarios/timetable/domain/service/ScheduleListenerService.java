package br.com.gradehorarios.gradehorarios.timetable.domain.service;

import br.com.gradehorarios.gradehorarios.timetable.infra.dto.TimetableResultMessage;

public interface ScheduleListenerService {

        public void recibeveScheduleResponse(TimetableResultMessage message);
}
