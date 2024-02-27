package com.unloadhome.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Repo {
    private long owner_id;
    private String name;
    private boolean visible;
}
