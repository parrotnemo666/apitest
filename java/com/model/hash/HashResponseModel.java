package com.model.hash;


public class HashResponseModel {

    private String input;
    private String algorithm;
    private String hash;

    // Constructor
    public HashResponseModel(String input, String algorithm, String hash) {
        this.input = input;
        this.algorithm = algorithm;
        this.hash = hash;
    }

    // Getters
    public String getInput() {
        return input;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getHash() {
        return hash;
    }

	public void setInput(String input) {
		this.input = input;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
    
    
}
