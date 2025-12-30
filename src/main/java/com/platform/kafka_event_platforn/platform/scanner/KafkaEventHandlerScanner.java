package com.platform.kafka_event_platforn.platform.scanner;

import com.platform.kafka_event_platforn.platform.annotation.KafkaEventController;
import com.platform.kafka_event_platforn.platform.annotation.KafkaEventHandler;
import com.platform.kafka_event_platforn.platform.registry.EventHandlerMethod;
import com.platform.kafka_event_platforn.platform.registry.KafkaEventHandlerRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Component
public class KafkaEventHandlerScanner {

    private final ApplicationContext context;
    private final KafkaEventHandlerRegistry registry;

    public KafkaEventHandlerScanner(
            ApplicationContext context,
            KafkaEventHandlerRegistry registry
    ) {
        this.context = context;
        this.registry = registry;
    }

    @PostConstruct
    public void scan() {
        Map<String, Object> controllers =
                context.getBeansWithAnnotation(KafkaEventController.class);

        System.out.println("🔍 Found controllers: " + controllers.keySet());

        controllers.values().forEach(bean -> {
            Class<?> targetClass = bean.getClass();

            for (Method method : targetClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(KafkaEventHandler.class)) {

                    KafkaEventHandler annotation =
                            method.getAnnotation(KafkaEventHandler.class);

                    Class<?> payloadType = method.getParameterTypes()[0];

                    registry.register(
                            annotation.eventType(),
                            new EventHandlerMethod(bean, method, payloadType)
                    );
                }
            }
        });
    }
}


