package br.com.gradehorarios.gradehorarios.solution.application.service.Menssaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import br.com.gradehorarios.gradehorarios.bootstrap.config.MessagingConfig;
import br.com.gradehorarios.gradehorarios.solution.application.dto.TimetableResultMessage;

@Service
public class ScheduleConsumerService {
    
    @RabbitListener(queues = MessagingConfig.RESULT_QUEUE_NAME)
    public void recibeveScheduleResponse(TimetableResultMessage message) {
        System.out.println("Resultado Recebido para Job ID: " + message.id());
    }
}
