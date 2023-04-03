package com.frame.rpc.provider.netty;

import com.frame.rpc.provider.netty.server.ServerInboundHandler1;
import com.frame.rpc.provider.netty.server.ServerInboundHandler2;
import com.frame.rpc.provider.netty.server.ServerOutboundHandler1;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer {

    public static void main(String[] args) {
        NettyServer server = new NettyServer();
        server.start(18808);
    }

    public void start(int port) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(boss, worker)
                    //指定服务端通道，用于接收并创建新连接
                    .channel(NioServerSocketChannel.class)
                    // 给boss group 配置handler
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    //获取与该channel绑定的pipeline
                                    ChannelPipeline pipeline = socketChannel.pipeline();
                                    //像pipeline中添加handler
                                    pipeline.addLast(new ServerOutboundHandler1());
                                    pipeline.addLast(new ServerInboundHandler1());
                                    pipeline.addLast(new ServerInboundHandler2());
     /*
                                    ByteBuf buf = Unpooled.wrappedBuffer("$".getBytes(StandardCharsets.UTF_8));
//                                    换行解码器
//                                  pipeline.addLast(new LineBasedFrameDecoder(65536));
                                    pipeline.addLast(new DelimiterBasedFrameDecoder(65536, buf));

                           pipeline.addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,4));
                                    pipeline.addLast(new StringDecoder());
                                    pipeline.addLast(new TcpStickHalfHandler1());*/
                                }
                            }
                    );
            //服务端绑定端口启动
            ChannelFuture future = serverBootstrap.bind(port).sync();
            //服务端监听端口关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //优雅关闭 boss worker
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }


}
