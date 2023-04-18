package com.frame.rpc.provider.request;

import com.frame.rpc.provider.cache.ServiceProviderCache;
import com.frame.rpc.provider.cluster.LoadBalanceStrategy;
import com.frame.rpc.provider.cluster.StartegyProvider;
import com.frame.rpc.provider.config.RpcClientConfiguration;
import com.frame.rpc.provider.data.RpcRequest;
import com.frame.rpc.provider.data.RpcResponse;
import com.frame.rpc.provider.netty.codec.decode.RpcRequestDecoder;
import com.frame.rpc.provider.netty.codec.decode.RpcResponseDecoder;
import com.frame.rpc.provider.netty.codec.encode.FrameEncoder;
import com.frame.rpc.provider.netty.codec.encode.RpcRequestEncoder;
import com.frame.rpc.provider.netty.handler.RpcResponseHandler;
import com.frame.rpc.provider.netty.request.ChannelMapping;
import com.frame.rpc.provider.netty.request.RequestPromise;
import com.frame.rpc.provider.netty.request.RpcRequestHolder;
import com.frame.rpc.provider.provider.ServiceProvider;
import com.frame.rpc.provider.enums.StatusEnum;
import com.frame.rpc.provider.exception.RpcException;
import com.frame.rpc.provider.netty.codec.decode.FrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RpcRequestManager {

    @Resource
    private ServiceProviderCache providerCache;

    @Resource
    private StartegyProvider startegyProvider;

    @Resource
    private RpcClientConfiguration clientConfiguration;

    public RpcResponse sendRequest(RpcRequest request) {
        // 获取可用的服务节点
        List<ServiceProvider> serviceProviders = providerCache.get(request.getClassName());
        if (CollectionUtils.isEmpty(serviceProviders)) {
            log.info("客户端没有发现可用的服务提供者");
            throw new RpcException(StatusEnum.NOT_FOUND_SERVICE_PROVINDER);
        }
        // 挑选一个服务提供者
        // ServiceProvider serviceProvider = serviceProviders.get(0);
        LoadBalanceStrategy strategy = startegyProvider.getStrategy();
        //负载均衡
        ServiceProvider serviceProvider = strategy.select(serviceProviders);
        if (serviceProvider != null) {
            return requestByNetty(request, serviceProvider);
        } else {
            throw new RpcException(StatusEnum.NOT_FOUND_SERVICE_PROVINDER);
        }
    }

    private RpcResponse requestByNetty(RpcRequest request, ServiceProvider serviceProvider) {
        if (!RpcRequestHolder.channelExist(serviceProvider.getServerIp(), serviceProvider.getRpcPort())) {
            EventLoopGroup worker = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));
            RpcResponseHandler responseHandler = new RpcResponseHandler();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast("FrameEncoder", new FrameEncoder());
                            pipeline.addLast("RpcRequestEncoder", new RpcRequestEncoder());

                            pipeline.addLast("FrameDecoder", new FrameDecoder());
                            pipeline.addLast("RpcResponseDecoder", new RpcResponseDecoder());

                            //返回回来的对象
                            pipeline.addLast("RpcResponseHandler", responseHandler);


                        }
                    });
            try {
                ChannelFuture future = bootstrap.connect(serviceProvider.getServerIp(), serviceProvider.getRpcPort()).sync();
                if (future.isSuccess()) {
                    RpcRequestHolder.addChannelMapping(new ChannelMapping(serviceProvider.getServerIp(), serviceProvider.getRpcPort(), future.channel()));
                }
            } catch (Exception e) {
                log.error("客户端创建连接失败，msg={}", e.getMessage());
            }
        }

        Channel channel = RpcRequestHolder.getChannel(serviceProvider.getServerIp(), serviceProvider.getRpcPort());
        try {
            RequestPromise requestPromise = new RequestPromise(channel.eventLoop());
            RpcRequestHolder.addRequestPromise(request.getRequestId(), requestPromise);
            ChannelFuture channelFuture = channel.writeAndFlush(request);// writeAndFlush异步
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    // 如果数据没有发送成功，移除该请求对应的promise
                    if (!future.isSuccess()) {
                        RpcRequestHolder.removeRequestPromise(request.getRequestId());
                    }
                }
            });

            requestPromise.addListener(new FutureListener<RpcResponse>() {
                @Override
                public void operationComplete(Future<RpcResponse> future) throws Exception {
                    // 如果没有响应成功，移除 requestpromise
                    if (!future.isSuccess()) {
                        RpcRequestHolder.removeRequestPromise(request.getRequestId());
                    }
                }
            });

            // 获取结果,get是阻塞的  异步转同步的问题
            RpcResponse response = (RpcResponse) requestPromise.get(clientConfiguration.getConnectTimeout(), TimeUnit.SECONDS);
            RpcRequestHolder.removeRequestPromise(request.getRequestId());
            return response;
        } catch (Exception e) {
            log.error("remote rpc request error,msg={}", e.getCause());
            throw new RpcException(e);
        }
    }
}
