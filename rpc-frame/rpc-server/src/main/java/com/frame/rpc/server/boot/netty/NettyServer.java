package com.frame.rpc.server.boot.netty;


import com.frame.rpc.server.boot.RpcServer;
import com.frame.rpc.server.config.RpcServerConfiguration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.channels.ServerSocketChannel;

/**
 * @description
 * @author: ts
 * @create:2021-06-08 21:30
 */
@Slf4j
@Component
public class NettyServer implements RpcServer {

    @Resource
    private RpcServerConfiguration rpcServerConfiguration;

    @Override
    public void start() {
        EventLoopGroup boss = new NioEventLoopGroup(1, new DefaultThreadFactory("boss-"));
        EventLoopGroup worker = new NioEventLoopGroup(0, new DefaultThreadFactory("worker-"));
        UnorderedThreadPoolEventExecutor business = new UnorderedThreadPoolEventExecutor(
                NettyRuntime.availableProcessors() * 2, new DefaultThreadFactory("business--"));
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(
                        new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                ChannelPipeline pipeline = socketChannel.pipeline();

                            }
                        }
                )
        ;

    }


}
