package com.o0u0o.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.ByteBuffer;

/**
 * <h1></h1>
 *
 * @author o0u0o
 * @description
 * @since 2025/3/10 17:38
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping ("/world")
    public Object helloWorld(){
        return "Hello World";
    }

    @GetMapping("/occupy-memory")
    public String occupyMemory(@RequestParam int mb) {
        try {
            if (mb < 0) {
                return "Invalid memory size. Please provide a non-negative integer.";
            }
            // 计算需要的字节数
            long bytes = (long) mb * 1024 * 1024;
            // 创建一个字节数组来占用内存
            ByteBuffer buffer = ByteBuffer.allocateDirect((int) bytes);
            return "占用了内存 " + mb + " MB.";
        } catch (OutOfMemoryError e) {
            return "Failed to occupy " + mb + " MB of memory. Out of Memory!";
        } catch (IllegalArgumentException e) {
            return "设置的内存过大. 请输入一个更小的值.";
        }
    }

    @GetMapping("/gc")
    public String triggerGC() {
        System.gc();
        return "垃圾回收器(Garbage collection) 已经被触发.";
    }
}
