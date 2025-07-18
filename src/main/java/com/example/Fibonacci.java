package com.example;

import java.util.HashMap;
import java.util.Map;

public class Fibonacci {
    private final Map<Integer, Long> cache;

    public Fibonacci() {
        this.cache = new HashMap<>();
    }

    public void clearCache() {
        cache.clear();
    }

    public static void main(String... args) {
        Fibonacci fibonacci = new Fibonacci();
        System.out.println("Fibonacci Recursive " + fibonacci.Recursive(35));
        System.out.println("Fibonacci Memoized " + fibonacci.Memoized(35));
        System.out.println("Fibonacci Iterative " + fibonacci.Iterative(35));
    }

    public long Recursive(int n) {
        if (n < 2) {
            return validateNumberCondition(n);
        }

        return Recursive(n-1) + Recursive(n-2);
    }

    public long Memoized(int n) {
        if (n < 2) {
            return validateNumberCondition(n);
        }

        if (cache.containsKey(n)) {
            return cache.get(n);
        }

        return Memoized(n-1) + Memoized(n-2);
    }

    public long Iterative(int n) {
        if (n < 2) {
            return validateNumberCondition(n);
        }

        long prevNumber = 1;
        long prevPrevNumber = 0;
        long current = 0;

        for (int i = 1; i < n; i++) {
            current = prevNumber + prevPrevNumber;
            prevPrevNumber = prevNumber;
            prevNumber = current;
        }

        return current;
    }

    private long validateNumberCondition(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be greater than or equal to 0");
        }
        if (n <= 1) {
            return n;
        }

        return -1;
    }
}
