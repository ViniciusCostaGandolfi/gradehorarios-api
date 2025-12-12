package br.com.gradehorarios.gradehorarios.timetable.domain.service;

import br.com.gradehorarios.gradehorarios.timetable.infra.dto.TimetableRequestMessage;

public interface ScheduleProducerService {


        public void sendScheduleRequest(TimetableRequestMessage message) ;
}
