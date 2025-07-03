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
    }

    public long Recursive(int n) {
        if (n < 0) {
            log.severe("n must be greater than or equal to 0");
            throw new IllegalArgumentException("n must be greater than or equal to 0");
        }

        if (n <= 1) {
            return n;
        }

        long res = Recursive(n-1) + Recursive(n-2);
        log.fine("res = " + res + ", n = " + n);
        return res;
    }

    public long Memoized(int n) {
        if (n < 0) {
            log.severe("n must be greater than or equal to 0");
            throw new IllegalArgumentException("n must be greater than or equal to 0");
        }

        if (n <= 1) {
            return n;
        }

        if (cache.containsKey(n)) {
            return cache.get(n);
        }

        long res = Recursive(n-1) + Recursive(n-2);
        log.fine("res = " + res + ", n = " + n);
        cache.put(n, res);
        return res;
    }
}
