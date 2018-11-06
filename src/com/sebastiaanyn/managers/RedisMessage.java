package com.sebastiaanyn.managers;

public class RedisMessage<T> {
    final T obj;

    RedisMessage() {
        obj = null;
    }

    RedisMessage(T obj) {
        this.obj = obj;
    }
}
