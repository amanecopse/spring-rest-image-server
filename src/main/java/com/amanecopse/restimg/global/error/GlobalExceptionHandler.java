package com.amanecopse.restimg.global.error;

import com.amanecopse.restimg.global.common.response.ApiResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        logger.error("message", ex);
        String message = ex.getCause().getMessage();
        if (ex.getCause() instanceof InvalidFormatException) {
            message = "올바른 형식이어야 합니다";
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        logger.error("message", ex);
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map((error) -> "%s: [%s]".formatted(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(",\n"));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, FileUploadException.class})
    ResponseEntity<ApiResponse<Void>> handleBadRequestException(RuntimeException exception) {
        logger.error("message", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(exception.getMessage()));
    }

    @ExceptionHandler({FileNotFoundException.class})
    ResponseEntity<ApiResponse<Void>> handleNotFoundException(IOException exception) {
        logger.error("message", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("리소스를 찾을 수 없습니다"));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception exception) {
        logger.error("message", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("서버 내부 에러"));
    }
}
