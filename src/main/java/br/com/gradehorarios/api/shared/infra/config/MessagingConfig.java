package br.com.gradehorarios.api.shared.infra.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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
    
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue requestQueue() {
        return new Queue(REQUEST_QUEUE_NAME, true);
    }

    @Bean
    public Queue resultQueue() {
        return new Queue(RESULT_QUEUE_NAME, true);
    }

    @Bean
    public Binding bindingRequest(Queue requestQueue, DirectExchange exchange) {
        return BindingBuilder.bind(requestQueue)
                                .to(exchange)
                                .with(REQUEST_ROUTING_KEY);
    }

    @Bean
    public Binding bindingResult(Queue resultQueue, DirectExchange exchange) {
        return BindingBuilder.bind(resultQueue)
                                .to(exchange)
                                .with(RESULT_ROUTING_KEY);
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}