package com.o0u0o.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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

    public static SseEmitter connect(String userId) {
        //设置超时时间，0表示不过期。默认30秒，超时未完成任务则会抛出异常：TimeoutException
        SseEmitter sseEmitter = new SseEmitter(0L);

        //注册SSE的回调方法
        sseEmitter.onCompletion(completionCallback(userId));
        sseEmitter.onError(errorCallback(userId));
        sseEmitter.onTimeout(timeoutCallback(userId));

        sseClients.put(userId, sseEmitter);
        log.info("当前创建新的SSE连接，用户ID为：{}", userId);

        return sseEmitter;
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
    }
}
