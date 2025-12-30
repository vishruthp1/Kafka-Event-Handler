package com.platform.kafka_event_platforn.platform.registry;

import java.lang.reflect.Method;

public class EventHandlerMethod {

    private final Object bean;
    private final Method method;
    private final Class<?> payloadType;

    public EventHandlerMethod(Object bean, Method method, Class<?> payloadType) {
        this.bean = bean;
        this.method = method;
        this.payloadType = payloadType;
    }

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getPayloadType() {
        return payloadType;
    }
}

