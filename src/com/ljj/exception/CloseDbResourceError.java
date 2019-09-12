package com.ljj.exception;

public class CloseDbResourceError extends RuntimeException {
    public CloseDbResourceError(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "在释放数据库资源时出错："+super.getMessage();
    }
}
