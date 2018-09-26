package com.motorcli.springboot.web.error;

public interface CustomExceptionListener {

    void onException(Throwable ex);
}
