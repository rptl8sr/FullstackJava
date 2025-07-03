package com.example;

import lombok.extern.java.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class Fibonacci {
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s: %5$s%6$s%n");
        Logger.getLogger("Fibonacci").setLevel(Level.INFO);
    }

    public static void main(String... args) {
        System.out.println("FibonacciRecursive " + fibonacciRecursive(10));
    }

    public static long fibonacciRecursive(int n) {
        if (n < 0) {
            log.severe("n must be greater than or equal to 0");
            throw new IllegalArgumentException("n must be greater than or equal to 0");
        }

        if (n <= 1) {
            return n;
        }

        long res = fibonacciRecursive(n-1) + fibonacciRecursive(n-2);
        log.fine("res = " + res + ", n = " + n);
        return res;
    }
}
