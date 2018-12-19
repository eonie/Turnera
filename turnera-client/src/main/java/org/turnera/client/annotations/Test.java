package org.turnera.client.annotations;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

public class Test {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Reflections reflections = new Reflections();
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(TurneraJob.class);
        System.out.println("start");
        for (Class<?> turneraJob : annotated) {
            Method method = turneraJob.getMethod("execute", String.class);
            Object o = turneraJob.newInstance();
            method.invoke(o, "11111");
        }
    }
}
