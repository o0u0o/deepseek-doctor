package com.o0u0o.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <h1>跨域配置</h1>
 *
 * @author o0u0o
 * @since 2025/3/17 13:32
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
        // 允许所有的路径都开放跨域
        registry.addMapping("/**")
                .allowedOrigins("http://127.0.0.1:5500")    //允许改地址和端口访问
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
