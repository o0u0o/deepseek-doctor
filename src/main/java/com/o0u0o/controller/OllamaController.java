package com.o0u0o.controller;

import com.o0u0o.service.OllamaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * <h1>ollama接口</h1>
 *
 * @author o0u0o
 * @description 用于测试
 * @since 2025/3/11 00:06
 */
@Slf4j
@RestController
@RequestMapping("/ollama")
@Tag(name = "ollama接口(OllamaController)")
public class OllamaController {

    @Resource
    private OllamaService ollamaService;

    @GetMapping("/ai/chat")
    @Operation(summary = "Ollama聊天-同步调用")
    public Object aiOllamaChat(@RequestParam String msg){
        return ollamaService.aiOllamaChat(msg);
    }

    @GetMapping("/ai/stream1")
    @Operation(summary = "Ollama聊天-流式1")
    public Flux<ChatResponse> aiOllamaChat1(@RequestParam String msg){
        return ollamaService.aiOllamaStream1(msg);
    }

    @GetMapping("/ai/stream2")
    @Operation(summary = "Ollama聊天-流式2")
    public List<String> aiOllamaStream2(@RequestParam String msg){
        return ollamaService.aiOllamaStream2(msg);
    }


}
