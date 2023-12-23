package com.example.web.exceptions;

public class ErrorResponse {
    private String error;
    private Integer status;

    public ErrorResponse(String error, String status) {
        this.error = error;
        this.status = Integer.valueOf(status);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
