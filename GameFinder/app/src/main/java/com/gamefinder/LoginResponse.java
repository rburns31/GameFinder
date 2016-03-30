package com.gamefinder;

import java.util.List;

/**
 *
 * Created by Kevin on 2/26/2016.
 */
public class LoginResponse {
    private List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}