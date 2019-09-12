package com.ljj.exception;

public class ConnectToDbError extends RuntimeException {
    public ConnectToDbError(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "can connect to database:"+super.getMessage();
    }
}
