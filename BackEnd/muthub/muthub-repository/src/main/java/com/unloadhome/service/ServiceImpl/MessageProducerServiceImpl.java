package com.unloadhome.service.ServiceImpl;

import com.unloadhome.common.Result;
import com.unloadhome.model.MailMessage;
import com.unloadhome.service.MessageProduceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;

@Service
public class MessageProducerServiceImpl implements MessageProduceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducerServiceImpl.class);

    @Autowired
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public MessageProducerServiceImpl(KafkaTemplate<String, Object> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void SyncSendMessage(String topic, Object obj){
        try {
            SendResult<String, Object> sendResult = kafkaTemplate.send(topic, obj).get();
            if (sendResult.getRecordMetadata() != null) {
                LOGGER.info("生产者成功发送消息到" + sendResult.getProducerRecord().topic() + "-> " + sendResult.getProducerRecord().value().toString());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
     public void AsyncSendMessage(String topic, Object obj){
         ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, obj);
         future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
             @Override
             public void onFailure(Throwable ex) {
                 LOGGER.info("producer send message failed--{}--{}", obj.toString(), ex.getMessage());
             }

             @Override
             public void onSuccess(SendResult<String, Object> result) {
                LOGGER.info("producer send message" + obj.toString() + " to " + topic + "success: "
                + result.toString());
             }
         });
     }

    @Override
    public Result Watchnotification(long userID, long ownerId, String repoName) {
        MailMessage message = new MailMessage(userID, ownerId, repoName);
        AsyncSendMessage("watchMail", message);
        return Result.succ("send message success");
    }
}
