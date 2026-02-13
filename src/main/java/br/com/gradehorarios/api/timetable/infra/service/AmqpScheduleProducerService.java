package br.com.gradehorarios.api.timetable.infra.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gradehorarios.api.shared.infra.config.MessagingConfig;
import br.com.gradehorarios.api.timetable.domain.service.ScheduleProducerService;
import br.com.gradehorarios.api.timetable.infra.dto.TimetableRequestMessage;


@Service
public class AmqpScheduleProducerService implements ScheduleProducerService {

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
