package com.unloadhome.service;

import com.unloadhome.mapper.RepoMapper;
import com.unloadhome.mapper.UserMapper;
import com.unloadhome.model.MailMessage;
import com.unloadhome.model.Repo;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Value("${spring.mail.username}")
    private String sendMailer;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RepoMapper repoMapper;

    public void sendTextMailMessage(MailMessage mailMessage){
        String to = userMapper.selectUser(mailMessage.getUserID()).getEmail();
        String subject = "new action on repo " + mailMessage.getRepoName();
        String text = "There is a new operation on repo " + mailMessage.getRepoName() +
                " which you are watching now";
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage());
            mimeMessageHelper.setFrom(sendMailer);
            mimeMessageHelper.setTo(to.split(","));
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text);
            mimeMessageHelper.setSentDate(new Date());

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            LOGGER.info("send mail success to " + to);
        }catch (MessagingException ex){
            LOGGER.error("send mail failed\n" + ex.getMessage());
        }
    }

    public boolean preSendCheck(MailMessage mailMessage){
        if(userMapper.checkIdExist(mailMessage.getUserID()).size() == 1){
            if(userMapper.checkIdExist(mailMessage.getOwnerID()).size() == 1){
                List<Repo> result = repoMapper.getRepo(mailMessage.getOwnerID(), mailMessage.getRepoName());
                if(result.size() == 1){
                    return true;
                }else {
                    LOGGER.error("the repoName is invalid");
                    return false;
                }
            }else{
                LOGGER.error("the ID want to send email not found");
                return false;
            }
        }else{
            LOGGER.error("the ID to send to not found");
            return false;
        }
    }

}
