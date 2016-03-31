package com.gamefinder;

import java.util.List;

/**
 * Created by Kevin on 3/3/2016.
 */
public class LoginErrorResponse {
    private List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public List<String> setErrors(List<String> errors) {
        return this.errors = errors;
    }
}
