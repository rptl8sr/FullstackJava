package performance;

import java.util.*;
import java.util.function.Consumer;

import com.example.CustomList;
import org.junit.jupiter.api.BeforeEach;

import annotation.Test;

public class CustomListPerformanceTest {
    private Map<String, List<Integer>> testCases;
    private Map<String, Long> timeResults;
    private Map<String, Long> memoryResults;


    @BeforeEach
    void init() {
        testCases = new HashMap<>();
        testCases.put("ArrayList", new ArrayList<>());
        testCases.put("CustomList", new CustomList<>());
        testCases.put("LinkedList", new LinkedList<>());

        timeResults = new HashMap<>();
        memoryResults = new HashMap<>();
    }


    @Test(description = "Test add")
    void testBulkAdd() {
        runTest("Test add", (list) -> {
            int number = 10_000_000;
            for (int i = 0; i < number; i++) {
                list.add(i);
            }
        });
    }

    @Test(description = "Test add and remove")
    void testAddAndRemove() {
        runTest("Test add and remove", (list) -> {
            System.gc();
            int number = 10_000;
            for (int i = 0; i < number; i++) {
                list.add(i);
            }
            for (int i = 0; i < number; i++) {
                list.remove(0);
            }
        });
    }

    private void runTest(String testName, Consumer<List<Integer>> testLogic) {
        System.out.printf("**** %s ****%n", testName);
        Runtime runtime = Runtime.getRuntime();

        for (Map.Entry<String, List<Integer>> entry : testCases.entrySet()) {
            String name = entry.getKey();
            List<Integer> testCase = entry.getValue();

            System.out.printf("Test %s%n", name);

            System.gc();
            long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

            long startTime = System.currentTimeMillis();
            testLogic.accept(testCase);
            long duration = System.currentTimeMillis() - startTime;

            long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
            long memoryUsed = memoryAfter - memoryBefore;
            System.gc();

            timeResults.put(name, duration);
            memoryResults.put(name, memoryUsed);

            System.out.printf("Time elapsed: %d ms%n", duration);
            System.out.printf("Memory used: %d bytes (%.2f MB)%n", memoryUsed, memoryUsed / (1024.0 * 1024.0));
            System.out.println("================");
        }

        printResults(testName);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private void printResults(String testName) {
        System.out.printf("%n**** Results for time %s (percentages) ****%n", testName);
        Map<String, Long> sortedTime= sortByValue(timeResults);

        long minTime = sortedTime.values().iterator().next();
        for (Map.Entry<String, Long> entry : sortedTime.entrySet()) {
            String name = entry.getKey();
            Long duration = entry.getValue();
            double percentage = (double) duration / minTime * 100;
            System.out.printf("%s: %.2f%%%n", name, percentage);
        }
        System.out.println();

        System.out.printf("**** Results for memory %s (percentages) ****%n", testName);
        Map<String, Long> sortedMemory = sortByValue(timeResults);

        long minMemory = sortedMemory.values().iterator().next();
        for (Map.Entry<String, Long> entry : sortedMemory.entrySet()) {
            String name = entry.getKey();
            Long memory = entry.getValue();
            double percentage = (double) Math.abs(memory) / minMemory * 100;
            System.out.printf("%s: %.2f%% (%.2f MB)%n", name, percentage, memory / (1024.0 * 1024.0));
        }

        System.out.println();
        timeResults.clear();
        memoryResults.clear();
    }
}
