package com.luxshan.linkshort.exception;

public class LinkNotFoundException extends RuntimeException {

    public LinkNotFoundException(String shortCode) {
        super("Short link not found: " + shortCode);
    }
}
