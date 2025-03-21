package com.o0u0o.common.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * <h1>服务端推送事件</h1>
 * 如果是分布式环境下，SseEmitter能不能存到redis中呢？
 * SseEmitter如何在集群的SpringBoot中存在？
 * @author o0u0o
 * @description 服务端推送事件
 * @since 2025/3/11 13:15
 */
@Slf4j
public class SSEServer {

    // 使用Map存储客户端的SseEmitter,用于关联用户id和sse的连接
    private static Map<String, SseEmitter> sseClients = new ConcurrentHashMap<>();

    //在线人数 用于统计总在线人数
    private static AtomicInteger onlineCounts = new AtomicInteger(0);

    public static SseEmitter connect(String userId) {
        //设置超时时间，0表示不过期。默认30秒，超时未完成任务则会抛出异常：TimeoutException
        SseEmitter sseEmitter = new SseEmitter(0L);

        //注册SSE的回调方法
        sseEmitter.onCompletion(completionCallback(userId));
        sseEmitter.onError(errorCallback(userId));
        sseEmitter.onTimeout(timeoutCallback(userId));

        sseClients.put(userId, sseEmitter);
        log.info("当前创建新的SSE连接，用户ID为：{}", userId);

        onlineCounts.getAndIncrement();

        return sseEmitter;
    }

    /**
     * <h2>发送单条消息</h2>
     * @param userId 用户ID
     * @param message 消息
     */
    public static void sendMessage(String userId, String message, SSEMessageType msgType) {
        //判断sseClients是否为空
        if(CollectionUtils.isEmpty(sseClients)) {
            return;
        }

        if (sseClients.containsKey(userId)) {
            SseEmitter sseEmitter = sseClients.get(userId);
            sendEmitterMessage(sseEmitter, userId, message, SSEMessageType.MESSAGE);
        }
    }

    /**
     * <h2>发送消息给所有人</h2>
     * @param message 消息内容
     */
    public static void sendMessageToAllUsers(String message) {
        //判断sseClients是否为空
        if(CollectionUtils.isEmpty(sseClients)) {
            return;
        }

        sseClients.forEach((userId, sseEmitter) -> {
            sendEmitterMessage(sseEmitter, userId, message, SSEMessageType.MESSAGE);

        });

    }

    /**
     * <h2>使用SseEmitter推送消息</h2>
     * @param sseEmitter SseEmitter对象
     * @param userId 用户ID
     * @param message 消息
     * @param msgType 消息类型
     */

    public static void sendEmitterMessage(SseEmitter sseEmitter,
                                   String userId,
                                   String message,
                                   SSEMessageType msgType) {
        try {
            SseEmitter.SseEventBuilder msg = SseEmitter.event()
                    .id(userId)
                    .name(msgType.getType())
                    .data(message);
            sseEmitter.send(msg);
        } catch (IOException e) {
            log.info("用户[{}]的消息推送发生异常", userId);
            removeConnection(userId);
        }

    }

    /**
     * <h2>主动切断SSE和客户端的连接</h2>
     * @param userId  用户ID
     */
    public static void stopServer(String userId) {
        if(CollectionUtils.isEmpty(sseClients)) {
            return;
        }

        SseEmitter sseEmitter = sseClients.get(userId);
        if (sseEmitter != null){
            //complete 表示执行完毕，断开连接
            sseEmitter.complete();
            removeConnection(userId);
            log.info("用户[{}]的SSE链接关闭成功", userId);
        } else {
            log.warn("用户[{}]的SSE链接无需关闭，请勿重复操作", userId);
        }
    }

    /**
     * SSE链接完成后的回调方法（关闭链接的时候调用），
     * @param userId 用户ID
     * @return Runnable
     */
    private static Runnable completionCallback(String userId) {
        return () ->{
            log.info("SSE连接完成并结束，用户ID为：{}", userId);
            removeConnection(userId);
        };
    }


    /**
     * <h2>SSE连接发生错误时调用该回调</h2>
     * @param userId 用户ID
     * @return Runnable
     */
    private static Consumer<Throwable> errorCallback(String userId) {
        return Throwable ->{
            log.info("SSE连接发生错误，用户ID为：{}", userId);
            removeConnection(userId);
        };
    }


    /**
     * <h2>SSE连接超时时调用该回调</h2>
     * @param userId 用户ID
     * @return Runnable
     */
    private static Runnable timeoutCallback(String userId) {
        return () ->{
            log.info("SSE连接超时，用户ID为：{}", userId);
            removeConnection(userId);
        };
    }


    /**
     * <h2>从整个SSE服务中移除用户连接</h2>
     * @param userId 用户ID
     */
    public static void removeConnection(String userId) {
        sseClients.remove(userId);
        log.info("当前移除SSE连接，用户ID为：{}", userId);
        //断开后累减
        onlineCounts.getAndDecrement();
    }

    /**
     * <h2>获取当前在线人数</h2>
     * @return int 在线人数
     */
    public static int getOnlineCounts() {
        return onlineCounts.intValue();
    }
}
