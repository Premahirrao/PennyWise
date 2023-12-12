package com.trodev.pennywise;

public class Users {
    String mail,userName,password;
    public Users(String namee, String emaill, String password){

        this.userName = namee;
        this.mail = emaill;
        this.password = password;

    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public Users(){

    }
}
