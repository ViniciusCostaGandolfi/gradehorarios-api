package br.com.gradehorarios.api.shared.infra.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    public static final String EXCHANGE_NAME = "grade-horarios-exchange";
    public static final String REQUEST_QUEUE_NAME = "schedule-request-queue";
    public static final String RESULT_QUEUE_NAME = "schedule-result-queue";

    public static final String REQUEST_ROUTING_KEY = "schedule-request-queue";
    public static final String RESULT_ROUTING_KEY = "schedule-result-queue";
    
    // Queues and exchanges are now defined in module-specific configurations


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}