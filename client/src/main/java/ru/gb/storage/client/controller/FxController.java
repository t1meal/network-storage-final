package ru.gb.storage.client.controller;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ru.gb.storage.commons.handlers.JSonDecoder;
import ru.gb.storage.commons.handlers.JSonEncoder;
import ru.gb.storage.commons.messages.AuthorizationMessage;
import ru.gb.storage.commons.messages.FileRequestMessage;
import ru.gb.storage.commons.messages.Message;

import java.io.IOException;

public class FxController {
@FXML
    AnchorPane AnchorPaneAuth;
@FXML
    Button AuthButton;
@FXML
    TextField loginField;
@FXML
    PasswordField passwordField;

        private static final NioEventLoopGroup group = new NioEventLoopGroup(1);
        private static final int PORT = 9000;
        private static final String INET_HOST = "localhost";
        private boolean isAuthorized = false;


        public void tryToAuth(){
            AuthorizationMessage message = new AuthorizationMessage(loginField.getText(), passwordField.getText());

        }

        public void connect() {
            try {
                Bootstrap bootstrap = new Bootstrap()
                        .group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                ch.pipeline().addLast(
//                                    TRAFFIC_SHAPING_HANDLER,
                                        new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 3, 0, 3),
                                        new LengthFieldPrepender(3),
                                        new JSonDecoder(),
                                        new JSonEncoder(),
                                        new SimpleChannelInboundHandler<Message>() {

                                            @Override
                                            public void channelActive(ChannelHandlerContext ctx) {



//                                                FileRequestMessage frMsg = new FileRequestMessage();
//                                                frMsg.setPath("F:\\Eric Wing.pdf");
//                                                ctx.writeAndFlush(frMsg);
                                            }

                                            @Override
                                            protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws IOException {
                                                msg.handle(ctx);
                                            }
                                        }
                                );
                            }
                        });

                System.out.println("Client started");

                Channel channel = bootstrap.connect(INET_HOST, PORT).sync().channel();

                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
            }
        }

//        private void connect(){
//            stageAuthorized();
//
//        }

        private void stageAuthorized(){
            if (!isAuthorized){
                AnchorPaneAuth.setVisible(true);
                loginField.setManaged(true);
                passwordField.setManaged(true);
            } else{
                AnchorPaneAuth.setVisible(false);
                loginField.setManaged(false);
                passwordField.setManaged(false);
            }
        }

}



