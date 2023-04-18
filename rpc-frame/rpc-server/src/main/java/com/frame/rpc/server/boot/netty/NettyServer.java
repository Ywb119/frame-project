package com.frame.rpc.server.boot.netty;


import com.frame.rpc.provider.netty.codec.decode.FrameDecoder;
import com.frame.rpc.provider.netty.codec.decode.RpcRequestDecoder;
import com.frame.rpc.provider.netty.codec.encode.FrameEncoder;
import com.frame.rpc.provider.netty.codec.encode.RpcResponseEncoder;
import com.frame.rpc.provider.netty.handler.RpcRequestHandler;
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
        EventLoopGroup boss = new NioEventLoopGroup(1, new DefaultThreadFactory("boss"));
        EventLoopGroup worker = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));
        UnorderedThreadPoolEventExecutor business = new UnorderedThreadPoolEventExecutor(NettyRuntime.availableProcessors() * 2, new DefaultThreadFactory("business"));
        RpcRequestHandler rpcRequestHandler = new RpcRequestHandler();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("logHandler", new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast("FrameEncoder", new FrameEncoder());
                            pipeline.addLast("RpcResponseEncoder", new RpcResponseEncoder());

                            pipeline.addLast("FrameDecoder", new FrameDecoder());
                            pipeline.addLast("RpcRequestDecoder", new RpcRequestDecoder());
                            pipeline.addLast(business, "RpcRequestHandler", rpcRequestHandler);
                        }
                    });
            //绑定端口启动
            ChannelFuture future = serverBootstrap.bind(rpcServerConfiguration.getRpcPort()).sync();
            //阻塞等待关闭
            ChannelFuture sync = future.channel().closeFuture().sync();
            if (sync.isSuccess()) {
                log.info("服务启动成功");
            }
        } catch (Exception e) {
            log.error("rpc server start error,msg={}", e.getMessage());
        } finally {
            //释放资源
            business.shutdownGracefully();
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }

}
