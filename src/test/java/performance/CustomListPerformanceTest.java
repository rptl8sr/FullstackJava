package performance;

import com.example.CustomList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Consumer;

public class CustomListPerformanceTest {
    private Map<String, List<Integer>> testCases;
    private Map<String, Long> results;

    @BeforeEach
    void init() {
        testCases = new HashMap<>();
        testCases.put("ArrayList", new ArrayList<>());
        testCases.put("CustomList", new CustomList<>());
        testCases.put("LinkedList", new LinkedList<>());

        results = new HashMap<>();
    }


    @Test
    void testBulkAdd() {
        runTest("Test add", (list) -> {
            int number = 10_000_000;
            for (int i = 0; i < number; i++) {
                list.add(i);
            }
        });
    }

    @Test
    void testAddAndRemove() {
        runTest("Test add and remove", (list) -> {
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

        for (Map.Entry<String, List<Integer>> entry : testCases.entrySet()) {
            String name = entry.getKey();
            List<Integer> testCase = entry.getValue();

            System.out.printf("Test %s%n", name);

            long startTime = System.nanoTime();
            testLogic.accept(testCase);
            long endTime = System.nanoTime();

            long duration = endTime - startTime;
            results.put(name, duration);

            System.out.printf("Time elapsed: %d ns%n================%n", duration);
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
        if (results.isEmpty()) return;

        System.out.printf("%n**** Results for %s (percentages) ****%n", testName);
        Map<String, Long> sorted = sortByValue(results);

        long minTime = sorted.values().iterator().next();
        for (Map.Entry<String, Long> entry : sorted.entrySet()) {
            String name = entry.getKey();
            Long duration = entry.getValue();
            double percentage = (double) duration / minTime * 100;
            System.out.printf("%s: %.2f%%%n", name, percentage);
        }
        System.out.println();
    }
}
