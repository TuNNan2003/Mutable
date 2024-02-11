package com.unloadhome.dubbointerface;

import com.unloadhome.common.Status;

import java.io.Serializable;

public class IdResponse implements Serializable {
    private long id;
    private Status status;

    public IdResponse() {
    }

    public IdResponse(long id, Status status) {
        this.id = id;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("Result{");
        builder.append("id=").append(id);
        builder.append(", status=").append(status);
        builder.append('}');
        return builder.toString();
    }
}
