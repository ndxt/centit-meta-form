package com.centit.metaform.formaccess;

public class FieldValidator {
    private String message;

    public FieldValidator() {

    }

    public FieldValidator(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
