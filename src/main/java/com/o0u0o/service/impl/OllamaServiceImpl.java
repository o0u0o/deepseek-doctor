package com.o0u0o.service.impl;

import com.o0u0o.service.OllamaService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>Ollama接口实现</h1>
 *
 * @author o0u0o
 * @description
 * @since 2025/3/11 00:45
 */
@Slf4j
@Service
public class OllamaServiceImpl implements OllamaService {

    @Resource
    OllamaChatClient ollamaChatClient;

    @Override
    public Object aiOllamaChat(String msg) {
        // 这里是同步调用deepseek，当前页面会卡住，直到获得所以得数据才会返回给页面
        return ollamaChatClient.call(msg);
    }

    @Override
    public Flux<ChatResponse> aiOllamaStream1(String msg) {
        Prompt prompt = new Prompt(new UserMessage(msg));
        // 流式输出
        Flux<ChatResponse> streamResponse = ollamaChatClient.stream(prompt);
        return streamResponse;
    }

    /**
     * <h1>Ollama流式输出-文字处理</h1>
     *
     * @param msg 请求内容
     * @return Object
     */
    @Override
    public List<String> aiOllamaStream2(String msg) {
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
