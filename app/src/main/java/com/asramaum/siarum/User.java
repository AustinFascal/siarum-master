package com.asramaum.siarum;

/**
 * Created by austi on 6/22/2018.
 */

public class User {

    public String name, email, phone;
    public Boolean accountType;

    public User(String name, String email, String phone, Boolean accountType){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.accountType = accountType;
    }
}
