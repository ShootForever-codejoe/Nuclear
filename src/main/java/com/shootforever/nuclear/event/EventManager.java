package com.shootforever.nuclear.event;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private final Map<@NotNull Method, @NotNull Class<?>> registeredMethodMap;
    private final Map<@NotNull Method, @NotNull Object> methodObjectMap;
    private final Map<@NotNull Class<? extends Event>, @NotNull List<Method>> priorityMethodMap;

    public EventManager() {
        registeredMethodMap = new ConcurrentHashMap<>();
        methodObjectMap = new ConcurrentHashMap<>();
        priorityMethodMap = new ConcurrentHashMap<>();
    }

    public void register(@NotNull Object @NotNull ... objects) {
        for (Object object : objects){
            Class<?> clazz = object.getClass();
            Method[] methods = clazz.getDeclaredMethods();

            for (Method method : methods) {
                Annotation[] annotations = method.getDeclaredAnnotations();

                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == EventTarget.class && method.getParameterTypes().length == 1) {
                        registeredMethodMap.put(method, method.getParameterTypes()[0]);
                        methodObjectMap.put(method, object);

                        Class<? extends Event> eventClass = method.getParameterTypes()[0].asSubclass(Event.class);
                        priorityMethodMap.computeIfAbsent(eventClass, k -> new CopyOnWriteArrayList<>()).add(method);
                    }
                }
            }
        }
    }

    public void unregister(@NotNull Object obj) {
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (registeredMethodMap.containsKey(method)) {
                registeredMethodMap.remove(method);
                methodObjectMap.remove(method);
                Class<? extends Event> eventClass = method.getParameterTypes()[0].asSubclass(Event.class);
                List<Method> priorityMethods = priorityMethodMap.get(eventClass);
                if (priorityMethods != null) {
                    priorityMethods.remove(method);
                }
            }
        }
    }

    public void call(@NotNull Event event) {
        Class<? extends Event> eventClass = event.getClass();

        List<Method> methods = priorityMethodMap.get(eventClass);
        if (methods != null) {
            methods.sort(Comparator.comparingInt(method -> {
                EventTarget eventTarget = method.getAnnotation(EventTarget.class);
                return eventTarget.priority();
            }));

            for (Method method : methods) {
                Object obj = methodObjectMap.get(method);
                method.setAccessible(true);
                try {
                    method.invoke(obj, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}