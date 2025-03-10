package com.o0u0o.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1></h1>
 *
 * @author o0u0o
 * @description 用于测试
 * @since 2025/3/11 00:06
 */
@Slf4j
@RestController
@RequestMapping("/ollama")
public class OllamaController {

    @Resource
    OllamaChatClient ollamaChatClient;

    /**
     * <h2>测试调用Ollama本地模型</h2>
     *
     * @param msg 请求消息
     * @return Object
     */
    @GetMapping("/ai/chat")
    public Object aiOllamaChat(@RequestParam String msg){
        // 这里是同步调用deepseek，当前页面会卡住，直到获得所以得数据才会返回给页面
        String called = ollamaChatClient.call(msg);
        return called;
    }

    @GetMapping("/ai/stream1")
    public Flux<ChatResponse> aiOllamaChat1(@RequestParam String msg){
        Prompt prompt = new Prompt(new UserMessage(msg));
        // 流式输出
        Flux<ChatResponse> streamResponse = ollamaChatClient.stream(prompt);
        return streamResponse;
    }

    @GetMapping("/ai/stream2")
    public List<String> aiOllamaStream2(@RequestParam String msg){
        Prompt prompt = new Prompt(new UserMessage(msg));
        Flux<ChatResponse> streamResponse = ollamaChatClient.stream(prompt);

        //处理文字
        List<String> list = streamResponse.toStream().map(chatResponse -> {
            String content = chatResponse.getResult().getOutput().getContent();
            log.info(content);
            return content;
        }).collect(Collectors.toList());

        return list;
    }


}
