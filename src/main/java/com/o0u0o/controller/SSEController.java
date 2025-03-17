package com.o0u0o.controller;

import com.o0u0o.common.sse.SSEMessageType;
import com.o0u0o.common.sse.SSEServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * <h1>SSE服务端事件推送的接口</h1>
 *
 * @author o0u0o
 * @since 2025/3/17 12:34
 */
@Slf4j
@RestController
@RequestMapping("/sse")
public class SSEController {

    /**
     * <h2>SSE连接</h2>
     * @param userId 用户ID
     * @return SseEmitter 连接
     */
    @GetMapping(path = "connect", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter connect(@RequestParam String userId){
        log.info("SSE连接，用户ID为：{}", userId);
        return SSEServer.connect(userId);
    }

    /**
     * <h2>发送单条给消息SSE客户端</h2>
     * @param userId 用户ID
     * @param message 消息
     * @return 发送结果
     */
    @GetMapping( "sendMessage")
    public Object sendMessage(@RequestParam String userId,
                              @RequestParam String message){

        SSEServer.sendMessage(userId, message, SSEMessageType.MESSAGE);
        return "OK";
    }

    /**
     * <h2>发送给所有SSE客户端</h2>
     * @param message 消息内容
     * @return 发送结果
     */
    @GetMapping( "sendMessageAll")
    public Object sendMessageAll(@RequestParam String message){
        SSEServer.sendMessageToAllUsers( message);
        return "OK";
    }

    /**
     * <h2>发送追加消息给SSE客户端 ADD时间流式输出</h2>
     * @param userId 用户ID
     * @param message 消息内容
     * @return Object
     * @throws InterruptedException 线程中断异常
     */
    @GetMapping( "sendMessageAdd")
    public Object sendMessageAdd(@RequestParam String userId, @RequestParam String message) throws InterruptedException {
        //测试代码，循环输出内容
        for (int i = 0; i < 10; i++) {
            Thread.sleep(200);
            SSEServer.sendMessage(userId, message + "-" + i, SSEMessageType.ADD);
        }
        return "OK";
    }

}
