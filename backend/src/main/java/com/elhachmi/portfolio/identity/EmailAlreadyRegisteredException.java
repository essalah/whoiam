package com.elhachmi.portfolio.identity;

public class EmailAlreadyRegisteredException extends RuntimeException {

    public EmailAlreadyRegisteredException() {
        super("An account is already registered with this email");
    }
}
