package org.turnera.client.annotations;

import org.reflections.Reflections;

import java.util.Set;

public class Test {
    public static void main(String[] args) {
        Reflections reflections = new Reflections();
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(TurneraJob.class);
        for (Class<?> turneraJob : annotated) {
            System.out.println(turneraJob.getName());
        }
    }
}
