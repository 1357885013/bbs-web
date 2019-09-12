package com.ljj.exception;

public class CanFindDbConfig extends RuntimeException {
    @Override
    public String getMessage() {
        return "cannot find datebase config files!!!";
    }
}
