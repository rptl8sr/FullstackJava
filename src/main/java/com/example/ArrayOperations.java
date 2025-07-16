package com.example;

public class ArrayOperations {
    /**
     * Shift array elements using System.arraycopy
     */
    public static void shiftLeftSystemCopy(int[] array, int position, boolean clearTrailing) {
        shiftLeft(array, position, clearTrailing, ArrayOperations::systemCopy);
    }

    /**
     * Shift array elements using a manual for loop
     */
    public static void shiftLeftManualLoop(int[] array, int position, boolean clearTrailing) {
        shiftLeft(array, position, clearTrailing, ArrayOperations::manualLoop);
    }

    @FunctionalInterface // TODO: wow, OOP polymorphism. My lovely trick in go, create an interface and implement it with different logic
    private interface ShiftMethod {
        void shift(int[] array, int position);
    }

    private static void shiftLeft(int[] array, int position, boolean clearTrailing, ShiftMethod method) {
        validateParameters(array, position);

        position = position % array.length;
        if (position == 0) {
            return;
        }

        method.shift(array, position);

        if (clearTrailing) {
            clearTrailingElements(array, position);
        }
    }

    private static void validateParameters(int[] array, int shift) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }

        if (array.length == 0) {
            throw new IllegalArgumentException("Array size must be greater than 0");
        }
        
        if (shift <= 0) {
            throw new IllegalArgumentException("Shift must be greater than 0");
        }
    }

    private static void clearTrailingElements(int[] array, int position) {
        for (int i = array.length - position; i < array.length; i++) {
            array[i] = 0;
        }
    }

    private static void systemCopy(int[] array, int position) {
        System.arraycopy(array, position, array, 0, array.length - position);
    }

    private static void manualLoop(int[] array, int position) {
        for (int i = 0; i < array.length - position; i++) {
            array[i] = array[i + position];
        }
    }
}
