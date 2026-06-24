package com.elhachmi.portfolio.resume;

public class ResumeLimitReachedException extends RuntimeException {
    public ResumeLimitReachedException(String message) {
        super(message);
    }
}
