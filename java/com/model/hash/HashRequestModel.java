package com.model.hash;


import javax.validation.constraints.NotEmpty;

public class HashRequestModel {

    @NotEmpty
    private String input;

    @NotEmpty
    private String algorithm;

    // Getters and Setters
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
