package com.project.tda.models;

public class UsersDto {
    private String username;
    private String Firstname;

    public UsersDto(String username, String firstname) {
        this.username = username;
        Firstname = firstname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }
}
