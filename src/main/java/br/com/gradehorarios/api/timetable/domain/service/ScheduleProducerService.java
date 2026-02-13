package br.com.gradehorarios.api.timetable.domain.service;

import br.com.gradehorarios.api.timetable.infra.dto.TimetableRequestMessage;

public interface ScheduleProducerService {


        public void sendScheduleRequest(TimetableRequestMessage message) ;
}
