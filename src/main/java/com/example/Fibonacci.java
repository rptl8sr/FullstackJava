package com.example;

import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class Fibonacci {
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s: %5$s%6$s%n");
        Logger.getLogger("Fibonacci").setLevel(Level.INFO);
    }

    private final Map<Integer, Long> cache;

    public Fibonacci() {
        this.cache = new HashMap<>();
    }

    public static void main(String... args) {
        Fibonacci fibonacci = new Fibonacci();
        System.out.println("Fibonacci Recursive " + fibonacci.Recursive(10));
        System.out.println("Fibonacci Memoized " + fibonacci.Memoized(10));
        System.out.println("Fibonacci Iterative " + fibonacci.Iterative(10));
    }

    public long Recursive(int n) {
        if (n < 2) {
            return validateNumberCondition(n);
        }

        long res = Recursive(n-1) + Recursive(n-2);
        log.fine("res = " + res + ", n = " + n);
        return res;
    }

    public long Memoized(int n) {
        if (n < 2) {
            return validateNumberCondition(n);
        }

        if (cache.containsKey(n)) {
            return cache.get(n);
        }

        long res = Recursive(n-1) + Recursive(n-2);
        log.fine("res = " + res + ", n = " + n);
        cache.put(n, res);
        return res;
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
            log.fine("current = " + current + ", prevNumber = " + prevNumber + ", prevPrevNumber = " + prevPrevNumber);
        }

        return current;
    }

    private long validateNumberCondition(int n) {
        if (n < 0) {
            log.severe("n must be greater than or equal to 0");
            throw new IllegalArgumentException("n must be greater than or equal to 0");
        }
        if (n <= 1) {
            return n;
        }

        return -1;
    }
}
