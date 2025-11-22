package br.com.gradehorarios.gradehorarios.solution.application.service.Menssaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import br.com.gradehorarios.gradehorarios.bootstrap.config.MessagingConfig;
import br.com.gradehorarios.gradehorarios.solution.application.dto.TimetableRequestMessage;
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
