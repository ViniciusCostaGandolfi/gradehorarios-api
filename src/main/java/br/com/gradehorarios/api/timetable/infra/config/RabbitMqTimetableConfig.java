package br.com.gradehorarios.api.timetable.infra.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.gradehorarios.api.shared.infra.config.MessagingConfig;

@Configuration
public class RabbitMqTimetableConfig {

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(MessagingConfig.EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue requestQueue() {
        return new Queue(MessagingConfig.REQUEST_QUEUE_NAME, true);
    }

    @Bean
    public Queue resultQueue() {
        return new Queue(MessagingConfig.RESULT_QUEUE_NAME, true);
    }

    @Bean
    public Binding bindingRequest(Queue requestQueue, DirectExchange exchange) {
        return BindingBuilder.bind(requestQueue)
                                .to(exchange)
                                .with(MessagingConfig.REQUEST_ROUTING_KEY);
    }

    @Bean
    public Binding bindingResult(Queue resultQueue, DirectExchange exchange) {
        return BindingBuilder.bind(resultQueue)
                                .to(exchange)
                                .with(MessagingConfig.RESULT_ROUTING_KEY);
    }

}
