package org.aist.aide.mappingservice.domain.exceptions;

public class ValidationFailureException extends Exception {
    public ValidationFailureException(String str) {
        super(str);
    }
}
