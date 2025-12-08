package br.com.gradehorarios.gradehorarios.application.service.solution.listener;

import br.com.gradehorarios.gradehorarios.application.dto.solution.TimetableResultMessage;

public interface IScheduleListenerService {

        public void recibeveScheduleResponse(TimetableResultMessage message);
}
