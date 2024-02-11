package com.unloadhome.model;

import com.unloadhome.common.Status;
import lombok.Data;

@Data
public class regResponse {
    private long id;
    private Status status;

    public regResponse(long id, Status status){
        this.id = id;
        this.status = status;
    }

    @Override
    public String toString() {
        return "regResponse{" +
                "id=" + id +
                ", status=" + status +
                '}';
    }
}
