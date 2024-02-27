package com.unloadhome.model;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

@Data
public class MailMessage {
    private long userID;
    private long ownerID;
    private String repoName;

}
