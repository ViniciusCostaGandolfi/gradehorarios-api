package br.com.gradehorarios.gradehorarios.application.service.solution.producer;

import br.com.gradehorarios.gradehorarios.application.dto.solution.TimetableRequestMessage;

public interface IScheduleProducerService {


        public void sendScheduleRequest(TimetableRequestMessage message) ;
}
