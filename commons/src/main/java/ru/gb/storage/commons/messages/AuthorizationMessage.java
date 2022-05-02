package ru.gb.storage.commons.messages;

import io.netty.channel.ChannelHandlerContext;


import java.io.IOException;

public class AuthorizationMessage extends Message{

    private String login;
    private String password;
    private boolean AuthorizationStatus = false;

    public AuthorizationMessage(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean getAuthorizationStatus() {
        return AuthorizationStatus;
    }

    public void setAuthorizationStatus(boolean authorizationStatus) {
        AuthorizationStatus = authorizationStatus;
    }

    @Override
    public void handle(ChannelHandlerContext ctx) throws IOException{
        System.out.println("new auth message");
        if (getLogin().equals("login1") && getPassword().equals("pass1")){
            setAuthorizationStatus(true);
        }
    }
}



