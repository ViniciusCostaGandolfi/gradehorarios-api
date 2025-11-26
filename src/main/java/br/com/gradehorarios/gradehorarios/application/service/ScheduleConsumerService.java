package br.com.gradehorarios.gradehorarios.application.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import br.com.gradehorarios.gradehorarios.application.dto.TimetableResultMessage;
import br.com.gradehorarios.gradehorarios.bootstrap.config.MessagingConfig;

@Service
public class ScheduleConsumerService {
    
    @RabbitListener(queues = MessagingConfig.RESULT_QUEUE_NAME)
    public void recibeveScheduleResponse(TimetableResultMessage message) {
        System.out.println("Resultado Recebido para Job ID: " + message.id());
    }
}
