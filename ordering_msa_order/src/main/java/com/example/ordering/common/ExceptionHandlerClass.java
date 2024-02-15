package com.example.ordering.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

import static com.example.ordering.common.ErrResponseMessage.makeMessage;

@RestController
@Slf4j
public class ExceptionHandlerClass {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex) {
        log.error("Entity not found : " + ex.getMessage());
        return makeMessage(HttpStatus.NOT_FOUND, "요청한 내용을 찾을 수 없습니다");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleEntityIliegalArgument(IllegalArgumentException ex) {
        log.error("Entity not found : " + ex.getMessage());
        return makeMessage(HttpStatus.NOT_ACCEPTABLE, "잘못된 접근 입니다. ");
    }
}
