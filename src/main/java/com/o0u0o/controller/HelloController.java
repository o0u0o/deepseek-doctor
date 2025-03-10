package com.o0u0o.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
