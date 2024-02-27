package com.unloadhome.model;

import lombok.Data;

@Data
public class UserRepoAct {
    private long userID;
    private long owner_id;
    private String repo_name;
    private boolean star;
    private boolean watch;
    private boolean coolaborator;
}
