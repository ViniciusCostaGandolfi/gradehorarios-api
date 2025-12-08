package br.com.gradehorarios.gradehorarios.application.service.solution.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gradehorarios.gradehorarios.application.dto.solution.TimetableRequestMessage;
import br.com.gradehorarios.gradehorarios.bootstrap.config.MessagingConfig;


@Service
public class ScheduleProducerService implements IScheduleProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendScheduleRequest(TimetableRequestMessage message) {
        rabbitTemplate.convertAndSend(
            MessagingConfig.EXCHANGE_NAME,
            MessagingConfig.REQUEST_ROUTING_KEY,
            message
        );
    }
    
}
