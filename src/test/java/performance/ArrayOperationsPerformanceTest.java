package performance;

import com.example.ArrayOperations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class ArrayOperationsPerformanceTest {
    private final static int[] ARRAY_SIZES = {10, 100, 1000, 10_000, 100_000, 1_000_000};
    private final static int[] SHIFT_POSITIONS = {1, 10, 100, 1000};
    private final static boolean[] CLEAR_TRAILING = {true, false};
    private final Map<String, Long> timeResults = new HashMap<>();

    private int[] generateArray(int size) {
        int[] array = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(1000);
        }
        return array;
    }

    @FunctionalInterface
    private interface ShiftMethod {
        void shift(int[] array, int position, boolean clearTrailing);
    }

    private void testShiftMethod(String methodName, ShiftMethod shiftMethod, int[] originalArray, int size, int shift, boolean clearTrailing) {
        int[] arrayCopy = originalArray.clone();
        String key = String.format("%s_size%d_shift%d_trailing%s", methodName, size, shift, clearTrailing);
        try {
            long startTime = System.nanoTime();
            shiftMethod.shift(arrayCopy, shift, clearTrailing);
            timeResults.put(key, System.nanoTime() - startTime);
        } catch (IllegalArgumentException e) {
            System.out.println("Unexpected exception for " + key + ": " + e.getMessage());
        }
    }

    @BeforeEach
    void init() {
        timeResults.clear();
    }

    @Test
    void testShiftPerformance() {
        for (int arraySize : ARRAY_SIZES) {
            int[] originalArray = generateArray(arraySize);
            for (int shiftPosition : SHIFT_POSITIONS) {
                if (shiftPosition >= arraySize) {
                    continue;
                }
                for (boolean clearTrailing : CLEAR_TRAILING) {
                    testShiftMethod("SystemCopy", ArrayOperations::shiftLeftSystemCopy, originalArray, arraySize, shiftPosition, clearTrailing);
                    testShiftMethod("ManualLoop", ArrayOperations::shiftLeftManualLoop, originalArray, arraySize, shiftPosition, clearTrailing);
                }
            }
        }
    }

    @Test
    void printPerformanceReport() {
        testShiftPerformance();

        System.out.println("Performance Comparison Report");
        System.out.println("=====================================");
        System.out.println("Size     | Shift    | Trailing | SystemCopy Time (ns) | ManualLoop Time (ns)");
        System.out.println("---------|----------|----------|---------------------|---------------------");

        List<String> sortedKeys = new ArrayList<>(timeResults.keySet());
        sortedKeys.sort((a, b) -> {
            String[] partsA = a.split("_");
            String[] partsB = b.split("_");
            int sizeA = Integer.parseInt(partsA[1].replace("size", ""));
            int sizeB = Integer.parseInt(partsB[1].replace("size", ""));
            if (sizeA != sizeB) return Integer.compare(sizeA, sizeB);
            int shiftA = Integer.parseInt(partsA[2].replace("shift", ""));
            int shiftB = Integer.parseInt(partsB[2].replace("shift", ""));
            if (shiftA != shiftB) return Integer.compare(shiftA, shiftB);
            return partsA[3].compareTo(partsB[3]);
        });

        for (String key : sortedKeys) {
            if (!key.startsWith("SystemCopy")) continue;
            String[] parts = key.split("_");
            int size = Integer.parseInt(parts[1].replace("size", ""));
            int shift = Integer.parseInt(parts[2].replace("shift", ""));
            String trailing = parts[3].replace("trailing", "");
            String manualKey = String.format("ManualLoop_size%d_shift%d_trailing%s", size, shift, trailing);
            if (!timeResults.containsKey(manualKey)) continue;
            System.out.printf("%-8d | %-8d | %-8s | %-19d | %-19d%n",
                    size, shift, trailing,
                    timeResults.get(key), timeResults.get(manualKey));
        }

        System.out.println("\nComplexity Analysis");
        System.out.println("==================");
        System.out.println("System.arraycopy: O(n) - Native bulk copy, faster due to optimized memory operations.");
        System.out.println("Manual Loop: O(n) - Slower due to per-element copying in Java.");
        System.out.println("Performance: System.arraycopy typically outperforms manual loop.");
    }
}