package com.unloadhome.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unloadhome.model.MailMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.protocol.types.Struct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumerService {
    @Value("${kafka.topic.Watch-Mail}")
    private String MailTopic;

    @Autowired
    private MailService mailService;
    private static Logger LOGGER = LoggerFactory.getLogger(MessageConsumerService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = {"${kafka.topic.Watch-Mail}"}, groupId = "group1")
    public void consumeMessage(ConsumerRecord<String, String> ConsumerRecord){
        try {
            MailMessage message = objectMapper.readValue(ConsumerRecord.value(), MailMessage.class);
            LOGGER.info("consumer get topic:{}, partition:{}, message:{}",
                    ConsumerRecord.topic(), ConsumerRecord.partition(), message.toString()
            );
            if(mailService.preSendCheck(message)){
                mailService.sendTextMailMessage(message);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
