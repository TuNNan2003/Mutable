package com.unloadhome.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailMessage {
    private long userID;
    private long ownerID;
    private String repoName;

}
