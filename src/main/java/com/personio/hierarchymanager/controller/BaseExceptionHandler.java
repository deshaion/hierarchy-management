package com.personio.hierarchymanager.controller;

import com.personio.hierarchymanager.exception.LoopInRequestException;
import com.personio.hierarchymanager.exception.MultiplyRootException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class BaseExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public final ErrorDetails handleAllExceptions(Throwable ex, WebRequest request) {
        return new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler({MultiplyRootException.class, LoopInRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ErrorDetails handleBadRequest(RuntimeException ex, WebRequest request) {
        return new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorDetails {
        private Date timestamp;
        private String message;
        private String details;
    }
}
