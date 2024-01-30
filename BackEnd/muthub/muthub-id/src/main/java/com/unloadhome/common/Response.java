package com.unloadhome.common;

public class Response {
    private long id;
    private Status status;

    public Response() {
    }

    public Response(long id, Status status) {
        this.id = id;
        this.status = status;
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
