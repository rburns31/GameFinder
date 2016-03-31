package com.gamefinder;

import java.util.List;
import java.util.Map;

/**
 * Created by Kevin on 2/26/2016.
 */
public class LoginResponse {
    private List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public List<String> setErrors(List<String> errors) {
        return this.errors = errors;
    }
}
