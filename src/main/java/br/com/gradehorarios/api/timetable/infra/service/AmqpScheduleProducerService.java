package br.com.gradehorarios.api.timetable.infra.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import br.com.gradehorarios.api.shared.infra.config.MessagingConfig;
import br.com.gradehorarios.api.timetable.domain.service.ScheduleProducerService;
import br.com.gradehorarios.api.timetable.infra.dto.TimetableRequestMessage;


@Service
@RequiredArgsConstructor
public class AmqpScheduleProducerService implements ScheduleProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendScheduleRequest(TimetableRequestMessage message) {
        rabbitTemplate.convertAndSend(
            MessagingConfig.EXCHANGE_NAME,
            MessagingConfig.REQUEST_ROUTING_KEY,
            message
        );
    }
    
}
