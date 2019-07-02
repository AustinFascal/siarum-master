package com.asramaum.siarum;

/**
 * Created by austi on 6/22/2018.
 */

public class User {

    public String name, nim, origin, email, phone, genderType;
    public Boolean accountType;

    public User(String name, String nim, String origin, String email, String phone, Boolean accountType, String genderType){
        this.name = name;
        this.nim = nim;
        this.origin = origin;
        this.email = email;
        this.phone = phone;
        this.accountType = accountType;
        this.genderType = genderType;
    }
}
