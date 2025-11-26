package br.com.gradehorarios.gradehorarios.application.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import br.com.gradehorarios.gradehorarios.application.dto.TimetableRequestMessage;
import br.com.gradehorarios.gradehorarios.bootstrap.config.MessagingConfig;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleProducerService {
    
    private final RabbitTemplate rabbitTemplate;

    public void sendScheduleRequest(TimetableRequestMessage message) {
        rabbitTemplate.convertAndSend(
            MessagingConfig.EXCHANGE_NAME,
            MessagingConfig.REQUEST_ROUTING_KEY,
            message
        );
    }
}
