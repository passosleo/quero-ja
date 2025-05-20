package com.leonardo.queroja.global;

import android.app.Application;
import android.content.Context;

import com.leonardo.queroja.entities.UserEntity;

public class UserSession extends Application {

    private UserEntity user;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public static UserSession getInstance(Context context) {
        return (UserSession) context.getApplicationContext();
    }
}
