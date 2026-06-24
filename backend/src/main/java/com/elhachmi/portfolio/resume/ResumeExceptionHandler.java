package com.elhachmi.portfolio.resume;

import com.elhachmi.portfolio.exception.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = ResumeController.class)
public class ResumeExceptionHandler {
    @ExceptionHandler(ResumeLimitReachedException.class)
    public ResponseEntity<ApiError> handleResumeLimit(
            ResumeLimitReachedException exception,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                "RESUME_LIMIT_REACHED",
                exception.getMessage(),
                request.getRequestURI(),
                Instant.now(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
}
