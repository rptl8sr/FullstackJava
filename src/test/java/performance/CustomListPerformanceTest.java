package performance;

import com.example.CustomList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class CustomListPerformanceTest {
    private Map<String, List<Integer>> testCases;

    @BeforeEach
    void init() {
        testCases = new HashMap<>();
        testCases.put("CustomList", new CustomList<>());
        testCases.put("ArrayList", new ArrayList<>());
        testCases.put("LinkedList", new LinkedList<>());
    }


    @Test
    void testBulkAdd() {
        int number = 10_000_000;

        System.out.println("**** Test add ****");
        for (Map.Entry<String, List<Integer>> entry : testCases.entrySet()) {
            String name = entry.getKey();
            List<Integer> testCase = entry.getValue();

            System.out.printf("Test %s%n", name);
            long startTime = System.nanoTime();


            for (int i = 0; i < number; i++) {
                testCase.add(i);
            }

            long endTime = System.nanoTime();
            System.out.printf("Time elapsed: %d ns%n================%n", endTime - startTime);
       }
    }

    @Test
    void testAddAndRemove() {
        int number = 10_000;

        System.out.println("**** Test add and remove ****");
        for (Map.Entry<String, List<Integer>> entry : testCases.entrySet()) {
            String name = entry.getKey();
            List<Integer> testCase = entry.getValue();

            System.out.printf("Test %s%n", name);
            long startTime = System.nanoTime();


            for (int i = 0; i < number; i++) {
                testCase.add(i);
            }

            for (int i = 0; i < number; i++) {
                testCase.remove(0);
            }

            long endTime = System.nanoTime();
            System.out.printf("Time elapsed: %d ns%n================%n", endTime - startTime);
        }
    }
}
