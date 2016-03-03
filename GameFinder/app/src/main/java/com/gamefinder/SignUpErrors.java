package com.gamefinder;

import java.util.List;

/**
 * Created by Kevin on 3/3/2016.
 */
public class SignUpErrors {
    List<String> full_messages;

    private List<String> getFullMessages() {
        return full_messages;
    }

    private List<String> setFullMessages(List<String> full_messages) {
        return this.full_messages = full_messages;
    }
}
