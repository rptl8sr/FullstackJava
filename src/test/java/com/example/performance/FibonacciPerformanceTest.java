package com.example.performance;

import com.example.Fibonacci;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;

import com.example.annotation.Test;

public class FibonacciPerformanceTest {
    private final int[] inputs = {10, 20, 30, 35, 37};
    private Fibonacci fibonacci;
    private Map<String, Function<Integer, Long>> algorithms;
    private final String[] algorithmOrder = {"Iterative", "Memoized", "Recursive"};

    @BeforeEach
    public void init() {
        this.fibonacci = new Fibonacci();
        this.algorithms = new HashMap<>();
        algorithms.put("Iterative", fibonacci::Iterative);
        algorithms.put("Memoized", fibonacci::Memoized);
        algorithms.put("Recursive", fibonacci::Recursive);

        for (int n : inputs) {
            for (String methodName : algorithmOrder) {
                algorithms.get(methodName).apply(n);
            }
            fibonacci.clearCache();
        }
    }

    @Test(timeout = 10, description = "Test com.example.performance of different methods to count fibonacci numbers")
    void testPerformance() {
        long[][] timeResults = new long[inputs.length][algorithmOrder.length];

        for (int i = 0; i < inputs.length; i++) {
            int n = inputs[i];
            fibonacci.clearCache();

            for (int j = 0; j < algorithmOrder.length; j++) {
                String methodName = algorithmOrder[j];
                Function<Integer, Long> algorithm = algorithms.get(methodName);
                long totalTime = 0;
                int iterations = 10;

                for (int k = 0; k < iterations; k++) {
                    long start = System.nanoTime();
                    algorithm.apply(n);
                    totalTime += System.nanoTime() - start;
                }
                timeResults[i][j] = totalTime / iterations / 1_000_000; // Среднее время в миллисекундах
            }
        }

        System.out.println("\nPerformance Time Test Results (Time in ms):");
        System.out.printf("%-10s%-12s%-12s%-12s%n", "n", algorithmOrder[0], algorithmOrder[1], algorithmOrder[2]);
        System.out.println("-".repeat(46));
        for (int i = 0; i < inputs.length; i++) {
            System.out.printf("%-10d%-12d%-12d%-12d%n", inputs[i], timeResults[i][0], timeResults[i][1], timeResults[i][2]);
        }
    }

    @Test(timeout = 10, description = "Test memory usage of different methods to count fibonacci numbers")
    void testMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long[][] memoryResults = new long[inputs.length][algorithmOrder.length];
        int memoryIterations = 10;

        for (int i = 0; i < inputs.length; i++) {
            int n = inputs[i];
            fibonacci.clearCache();

            for (int j = 0; j < algorithmOrder.length; j++) {
                String methodName = algorithmOrder[j];
                Function<Integer, Long> algorithm = algorithms.get(methodName);
                long totalMemory = 0;

                runtime.gc();
                for (int k = 0; k < memoryIterations; k++) {
                    long startMemory = runtime.totalMemory() - runtime.freeMemory();
                    algorithm.apply(n);
                    totalMemory += (runtime.totalMemory() - runtime.freeMemory()) - startMemory;
                }
                memoryResults[i][j] = totalMemory / memoryIterations; // Средняя память в байтах
            }
        }

        System.out.println("\nPerformance Memory Test Results (Bytes):");
        System.out.printf("%-10s%-12s%-12s%-12s%n", "n", algorithmOrder[0], algorithmOrder[1], algorithmOrder[2]);
        System.out.println("-".repeat(46));
        for (int i = 0; i < inputs.length; i++) {
            System.out.printf("%-10d%-12d%-12d%-12d%n", inputs[i], memoryResults[i][0], memoryResults[i][1], memoryResults[i][2]);
        }
    }

}
