package ru.gb.storage.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import ru.gb.storage.commons.handlers.*;
import ru.gb.storage.commons.messages.*;


import java.io.*;
import java.util.concurrent.Executors;


public class ClientNotFx {
    //    private static final GlobalChannelTrafficShapingHandler TRAFFIC_SHAPING_HANDLER = new GlobalChannelTrafficShapingHandler(Executors.newSingleThreadScheduledExecutor(),1000);
    private static final NioEventLoopGroup group = new NioEventLoopGroup(1);

    public static void main(String[] args) throws InterruptedException {
//        new Client().start();

//        Thread.sleep(3000);
//        while (true){
//            System.out.print("\r");
//            System.out.print(String.format ("Current speed: %.2f MB\\c" , ((double) TRAFFIC_SHAPING_HANDLER.trafficCounter().lastReadBytes() / 1024.0 / 1024.0)));
//            System.out.flush();
//            Thread.sleep(1000);
//        }
    }

    public void start() {
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
                                            FileRequestMessage frMsg = new FileRequestMessage();
                                            frMsg.setPath("F:\\Eric Wing.pdf");
                                            ctx.writeAndFlush(frMsg);

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

            Channel channel = bootstrap.connect("localhost", 9000).sync().channel();

//            while (channel.isActive()) {
//                TextMessage textMessage = new TextMessage();
//                textMessage.setText(String.format("[%s] %s", LocalDateTime.now(), Thread.currentThread().getName()));
//                System.out.println("Try to send message: " + textMessage);
//                channel.writeAndFlush(textMessage);
//
//                DateMessage dateMessage = new DateMessage();
//                dateMessage.setDate(new Date());
//                channel.write(dateMessage);
//                System.out.println("Try to send message: " + dateMessage);
//                channel.flush();
//                Thread.sleep(3000);
//            }

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}

