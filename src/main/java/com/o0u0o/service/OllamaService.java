package com.o0u0o.service;

import org.springframework.ai.chat.ChatResponse;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * <h1>Ollama服务接口</h1>
 *
 * @author o0u0o
 * @description
 * @since 2025/3/11 00:41
 */
public interface OllamaService {

    /**
     * <h1>Ollama请求</h1>
     * @param msg 请求内容
     * @return Object
     */
    public Object aiOllamaChat(String msg);

    public Flux<ChatResponse> aiOllamaStream1(String msg);

    /**
     * <h1>Ollama流式输出-文字处理</h1>
     * @param msg 请求内容
     * @return Object
     */
    public List<String> aiOllamaStream2(String msg);
}
