package com.unloadhome.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import javax.validation.Valid;

@Configuration
public class KafkaConfig {
    @Value("${kafka.topic.Watch-Mail}")
    private String MailTopic;

    @Bean
    public RecordMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }

    @Bean
    public NewTopic MailTopic() {
        return new NewTopic(MailTopic, 2, (short) 1);
    }

}
