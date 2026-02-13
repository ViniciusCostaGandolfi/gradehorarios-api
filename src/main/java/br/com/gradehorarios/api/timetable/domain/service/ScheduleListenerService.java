package br.com.gradehorarios.api.timetable.domain.service;

import br.com.gradehorarios.api.timetable.infra.dto.TimetableResultMessage;

public interface ScheduleListenerService {

        public void recibeveScheduleResponse(TimetableResultMessage message);
}
