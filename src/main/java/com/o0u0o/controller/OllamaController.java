package com.o0u0o.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1></h1>
 *
 * @author o0u0o
 * @description 用于测试
 * @since 2025/3/11 00:06
 */
@RestController
@RequestMapping("/ollama")
public class OllamaController {

    @Resource
    OllamaChatClient ollamaChatClient;

    @GetMapping("/ai/chat")
    public Object aiOllamaChat(@RequestParam String msg){
        String called = ollamaChatClient.call(msg);
        return called;
    }
}
