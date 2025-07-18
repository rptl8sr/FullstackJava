package com.example;

import com.example.annotation.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;


public class CustomTestRunner {
    private static final String defaultPackage = "com.example";
    static int total = 0;
    static int passed = 0;
    static int failed = 0;
    static long time = 0;

    public static void main(String[] args) {
        Class<?>[] testClasses = findAllTestClasses(defaultPackage);

        System.out.println("Testing classes");
        for (Class<?> c : testClasses) {
            System.out.printf("Class: %s\n", c.getSimpleName());

            Method beforeEach = null;
            Method afterEach = null;
            List<Method> testMethods = new ArrayList<>();

            Class<?> current = c;
            while (current != null && current != Object.class) {
                for (Method m : current.getDeclaredMethods()) {
                    if (m.getName().startsWith("lambda$")) {
                        continue;
                    }

                    if (m.isAnnotationPresent(BeforeEach.class)) {
                        System.out.printf("  Method: @BeforeEach %s\n", m.getName());
                        beforeEach = m;
                        continue;
                    }
                    if (m.isAnnotationPresent(AfterEach.class)) {
                        System.out.printf("  Method: @AfterEach %s\n", m.getName());
                        afterEach = m;
                        continue;
                    }
                    if (m.isAnnotationPresent(Test.class)) {
                        System.out.printf("  Method: @Test %s\n", m.getName());
                        testMethods.add(m);
                        total++;
                    } else {
                        System.out.printf("  Method: %s\n", m.getName());
                    }
                }

                current = current.getSuperclass();
            }

            runTests(c, beforeEach, afterEach, testMethods);
        }

        System.out.println("=== Test Results ===");
        System.out.printf("Total: %d\n", total);
        System.out.printf("Passed: %d\n", passed);
        System.out.printf("Failed: %d\n", failed);
        System.out.printf("Execution time: %d ms\n", time);
    }

    private static void runTests(Class<?> c, Method beforeEach, Method afterEach, List<Method> testMethods) {
        System.out.println("  Running tests:");

        for (Method m : testMethods) {
            Test testAnnotation = m.getAnnotation(Test.class);
            String description = testAnnotation.description();
            System.out.printf("    Running: %s - %s\n", m.getName(), description);
            long timeout = testAnnotation.timeout();
            long startTime = System.currentTimeMillis();

            // TODO: Oh, same as Python
            //      with open(RESOURCE)
            //  In Golang you should use same functionality like this:
            //      s := open(RESOURCE)
            //      defer close(RESOURCE)
            //  which ensures that the close function will be called anyway'
            try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
                Future<?> future = null;
                try {
                    Object testInstance = c.getDeclaredConstructor().newInstance();

                    if (beforeEach != null) {
                        beforeEach.setAccessible(true);
                        beforeEach.invoke(testInstance);
                    }
                    future = executor.submit(() -> {
                        try {
                            m.setAccessible(true);
                            m.invoke(testInstance);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });

                    if (timeout > 0) {
                        future.get(timeout, TimeUnit.SECONDS);
                    } else {
                        future.get();
                    }

                    if (afterEach != null) {
                        afterEach.setAccessible(true);
                        afterEach.invoke(testInstance);
                    }

                    executor.shutdownNow();

                    time += System.currentTimeMillis() - startTime;
                    passed++;
                    System.out.printf("    Time: %d ms\n", time);
                } catch (TimeoutException e) {
                    failed++;
                    System.out.printf("    %s FAILED: Timeout after %d ms\n", m.getName(), testAnnotation.timeout());
                } catch (Exception e) {
                    failed++;
                    String errorMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
                    System.out.printf("    %s FAILED: %s\n", m.getName(), errorMessage);
                } finally {
                    if (future != null) {
                        future.cancel(true);
                    }
                }
            }
        }
    }

    public static Class<?>[] findAllTestClasses(String basePackage) {
        Reflections reflections = new Reflections(basePackage, Scanners.TypesAnnotated, Scanners.MethodsAnnotated);

        System.out.println("Looking for test methods...");
        Set<Method> testMethods = reflections.getMethodsAnnotatedWith(Test.class);
        for (Method m : testMethods) {
            System.out.println("  Found: " + m.getDeclaringClass().getName() + "#" + m.getName());
        }

        Set<Class<?>> classes = new java.util.HashSet<>();
        for (Method m : testMethods) {
            classes.add(m.getDeclaringClass());
        }

        return classes.toArray(new Class<?>[0]);
    }
}
