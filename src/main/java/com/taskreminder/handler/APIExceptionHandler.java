package com.taskreminder.handler;

import com.taskreminder.dto.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;

@RestController
@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @ExceptionHandler(value = {ApiRequestException.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ErrorDTO  handleAPIExceptions(ApiRequestException ex) {
        logger.error("API exception occurred", ex.getCause());
        return new ErrorDTO(ex.getMessage(), ZonedDateTime.now());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class})
    public final ErrorDTO handleAllExceptions(Exception ex) {
        logger.error("Un Categorized exception", ex);
        return new ErrorDTO(ex.getCause().getMessage(), ZonedDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public final ErrorDTO handleConstraintExceptions(Exception ex) {
        logger.error("handleConstraintExceptions", ex);
        return new ErrorDTO("Already Exist or Invalid data", ZonedDateTime.now());
    }
}
