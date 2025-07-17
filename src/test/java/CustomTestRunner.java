import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import annotation.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;




public class CustomTestRunner {
    static int total = 0;
    static int passed = 0;
    static int failed = 0;
    static long time = 0;

    public static void main(String[] args) throws Exception {
        Class<?>[] testClasses = {
                CustomListTest.class,
                FibonacciTest.class,
                performance.ArrayOperationsPerformanceTest.class,
                performance.CustomListPerformanceTest.class,
                performance.FibonacciPerformanceTest.class
        };

        System.out.println("Testing classes");
        for (Class<?> c : testClasses) {
            System.out.printf("Class: %s\n", c.getSimpleName() );

            Method beforeEach = null;
            Method afterEach = null;
            List<Method> testMethods = new ArrayList<>();

            Method[] methods = c.getDeclaredMethods();
            for (Method m : methods) {
                if (m.getName().startsWith("lambda$")) {
                    continue;
                }

                if (m.isAnnotationPresent(BeforeEach.class)) {
                    System.out.printf("  Method: @BeforeEach %s\n", m.getName() );
                    beforeEach = m;
                    continue;
                }
                if (m.isAnnotationPresent(AfterEach.class)) {
                    System.out.printf("  Method: @AfterEach %s\n", m.getName() );
                    afterEach = m;
                    continue;
                }
                if (m.isAnnotationPresent(Test.class)) {
                    System.out.printf("  Method: @Test %s\n", m.getName() );
                    testMethods.add(m);
                    total++;
                } else {
                    System.out.printf("  Method: %s\n", m.getName() );
                }
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
            try {
                System.out.printf("    Method: %s\n", m.getName() );

                long startTime = System.currentTimeMillis();
                Object testInstance = c.getDeclaredConstructor().newInstance();

                if (beforeEach != null) {
                    beforeEach.setAccessible(true);
                    beforeEach.invoke(testInstance);
                }
                m.setAccessible(true);
                m.invoke(testInstance);
                if (afterEach != null) {
                    afterEach.setAccessible(true);
                    afterEach.invoke(testInstance);
                }

                time +=  System.currentTimeMillis() - startTime;

                passed++;
                System.out.printf("    Time: %d ms\n", time);
            } catch (Exception e) {
                failed++;
                String errorMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
                System.out.printf("    %s FAILED: %s\n", m.getName(), errorMessage);
            }
        }
    }
}
