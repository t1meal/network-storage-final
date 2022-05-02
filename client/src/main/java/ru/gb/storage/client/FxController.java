package ru.gb.storage.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.CharsetUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ru.gb.storage.commons.handlers.JSonDecoder;
import ru.gb.storage.commons.handlers.JSonEncoder;
import ru.gb.storage.commons.messages.AuthorizationMessage;
import ru.gb.storage.commons.messages.FileRequestMessage;
import ru.gb.storage.commons.messages.Message;
import ru.gb.storage.commons.messages.TextMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class FxController {
@FXML
    AnchorPane AuthorizationPane;
@FXML
    Button AuthButton;
@FXML
    TextField loginField;
@FXML
    PasswordField passwordField;
@FXML
    AnchorPane WorkingPane;
@FXML
    Button StartClient;


        private static final int PORT = 9000;
        private static final String INET_HOST = "localhost";
        private BooleanProperty connected = new SimpleBooleanProperty(false);
        private Channel channel;
        private NioEventLoopGroup group;


        public void tryToAuth() {
            System.out.println("touch Войти");
            AuthorizationMessage message = new AuthorizationMessage(loginField.getText(), passwordField.getText());

            send(message);

            if (message.getAuthorizationStatus()){
              connected.set(true);
              ChangeScene change = new ChangeScene();
              change.moveScene(AuthButton);
            }
        }

        public void send(Message message){

                if(!connected.get() ) {
                    return;
                }
                Task task = new Task() {
                    @Override
                    protected Void call() throws Exception {

                        ChannelFuture future = channel.writeAndFlush(new TextMessage());
                        future.sync();
                        return null;
                    }

                    @Override
                    protected void failed() {
//                        connected.set(false);
                    }
                };
                new Thread(task).start();
        }


        public void connect() {

            if (connected.get()){
                return;
            }
            group = new NioEventLoopGroup();

            Task<Channel> task = new Task<>() {
                @Override
                protected Channel call() throws Exception {

                    updateMessage("Bootstrapping");
                    updateProgress(0.1d, 1.0d);

                        Bootstrap bootstrap = new Bootstrap()
                                .group(group)
                                .channel(NioSocketChannel.class)
                                .remoteAddress(new InetSocketAddress(INET_HOST, PORT))
                                .option(ChannelOption.SO_KEEPALIVE, true)
                                .handler(new ChannelInitializer<SocketChannel>() {
                                    @Override
                                    protected void initChannel(SocketChannel ch) {
                                        ch.pipeline().addLast(
                                                new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 3, 0, 3),
                                                new LengthFieldPrepender(3),
                                                new JSonDecoder(),
                                                new JSonEncoder(),
                                                new ClientHandler() {
                                                }
                                        );
                                    }
                                });

                        System.out.println("Client started");

//                        Channel channel1 = bootstrap.connect(INET_HOST, PORT).sync().channel();
//                        channel1.closeFuture().sync();

//                        channel1.writeAndFlush(new TextMessage());

                        ChannelFuture future = bootstrap.connect();

                        future.sync();
                        Channel chn = future.channel();

                        updateMessage("Connecting");
                        updateProgress(0.2d, 1.0d);

                    return chn;
                }


                @Override
               protected void succeeded() {
                    channel = getValue();
                    connected.set(true);
                }
                @Override
                protected void failed() {
                    connected.set(false);
                }
            };
            new Thread(task).start();
        }

    public void disconnect() {

            if (!connected.get()){
                return;
            }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {

                updateMessage("Disconnecting");
                updateProgress(0.1d, 1.0d);

                channel.close().sync();
                updateMessage("Closing group");
                updateProgress(0.5d, 1.0d);

                group.shutdownGracefully().sync();
                return null;
            }

            @Override
            protected void succeeded() {

                connected.set(false);
            }
            @Override
            protected void failed(){
                connected.set(false);
            }

        };
        new Thread(task).start();
    }

}



