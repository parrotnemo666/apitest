package com.model;
public class ValidationResult {
    private boolean valid;
    private String correctID;

    public ValidationResult(boolean valid, String correctID) {
        this.valid = valid;
        this.correctID = correctID;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getCorrectID() {
        return correctID;
    }

    public void setCorrectID(String correctID) {
        this.correctID = correctID;
    }
}