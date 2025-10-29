package com.chineselearning.exception.custom;

/**
 * Exception thrown when request data is invalid
 * 
 * @author Senior Backend Architect
 */
public class InvalidRequestException extends RuntimeException {
    
    private final String field;
    private final Object rejectedValue;

    public InvalidRequestException(String message) {
        super(message);
        this.field = null;
        this.rejectedValue = null;
    }

    public InvalidRequestException(String message, String field, Object rejectedValue) {
        super(message);
        this.field = field;
        this.rejectedValue = rejectedValue;
    }

    public String getField() {
        return field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }
}

