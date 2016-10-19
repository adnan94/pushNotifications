package com.waqkz.cloudmessagingpushexample;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Waqas on 10/12/2016.
 */

public class LogInForm {

    private String UUID;
    private String userName;
    private String email;
    private String password;

    public LogInForm() {
    }

    public LogInForm(String UUID, String userName, String email, String password) {
        this.UUID = UUID;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*@Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", UUID);
        result.put("username", userName);
        result.put("email", email);
        result.put("password", password);

        return result;
    }*/

}
