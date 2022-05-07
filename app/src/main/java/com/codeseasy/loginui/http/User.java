package com.codeseasy.loginui.http;

public class User {

    int userId;      // id
    String userName;
    String password;
    String nickName;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName(){return nickName;}

    public void setNickName(String nickName) {this.nickName = nickName;}
}
