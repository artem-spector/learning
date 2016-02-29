package com.artem.learning.server.db;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * TODO: Document!
 *
 * @author artem on 2/28/16.
 */
public class User {

    @JsonProperty("login_id")
    private String loginId;

    @JsonProperty("display_name")
    private String displayName;

    public User() {
    }

    public User(String loginId, String displayName) {

        this.loginId = loginId;
        this.displayName = displayName;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || !(obj instanceof User)) return false;
        User that = (User) obj;
        return Arrays.equals(new Object[] {loginId, displayName}, new Object[] {that.loginId, that.displayName});
    }
}
