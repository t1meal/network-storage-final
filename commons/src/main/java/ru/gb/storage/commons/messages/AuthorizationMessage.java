package ru.gb.storage.commons.messages;



import io.netty.channel.ChannelHandlerContext;

public class AuthorizationMessage extends Message{

    private String login = "login1";
    private String password = "pass1";

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

    @Override
    public void handle(ChannelHandlerContext ctx) {
        String clientLogin = getLogin();
        String clientPassword = getPassword();
        if (this.toString().contains(clientLogin) && this.toString().contains(clientPassword)) {
            System.out.println("incoming authorization successful!");
            ctx.writeAndFlush("true");
        } else {
            System.out.println("incoming authorization unsuccessful!");
            ctx.writeAndFlush("false");
        }
    }
}
