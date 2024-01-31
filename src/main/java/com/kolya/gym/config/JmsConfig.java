package com.kolya.gym.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@EnableJms
@Configuration
public class JmsConfig {
    @Value("${mq.username}")
    private String MQ_USERNAME;

    @Value("${mq.password}")
    private String MQ_PASSWORD;

    @Value("${mq.url}")
    private String MQ_URL;

    @Value("${mq.converter.type-id-property-name}")
    private String TYPE_ID_PROPERTY_NAME;

    @Value("${mq.factory.concurrency}")
    private String CONCURRENCY;

    @Bean
    public ActiveMQConnectionFactory connectionFactory(){
        return new ActiveMQConnectionFactory(MQ_USERNAME, MQ_PASSWORD, MQ_URL);
    }

    @Bean
    public DefaultJmsListenerContainerFactory containerFactory(){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrency(CONCURRENCY);
        factory.setMessageConverter(converter());
        return factory;
    }

    @Bean
    public MessageConverter converter(){
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName(TYPE_ID_PROPERTY_NAME);
        return converter;
    }
}
