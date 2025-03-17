package com.o0u0o.utils;

import lombok.Getter;

/**
 * <h1>发送SSE的消息类型枚举类</h1>
 *
 * @author o0u0o
 * @since 2025/3/17 13:09
 */
@Getter
public enum SSEMessageType {
    MESSAGE("message", "单次发送的普通消息"),
    ADD("add", "消息追加，用于Stream流式推送"),
    FINISH("finish", "消息完成"),
    DONE("done", "消息完成");

    public final String type;
    public final String desc;

    SSEMessageType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
