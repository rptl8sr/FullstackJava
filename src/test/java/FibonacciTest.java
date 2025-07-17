import com.example.Fibonacci;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import annotation.Test;


public class FibonacciTest {
    private Fibonacci fibonacci;
    private final long[] first35fibNumbers = {
            0, 1, 1, 2, 3,
            5, 8, 13, 21, 34,
            55, 89, 144, 233, 377,
            610, 987, 1597, 2584, 4181,
            6765, 10946, 17711, 28657, 46368,
            75025, 121393, 196418, 317811, 514229,
            832040, 1346269, 2178309, 3524578, 5702887, 9227465
    };

    @BeforeEach
    public void setUp() {
        this.fibonacci = new Fibonacci();
    }

    @Test
    void testRecursiveCorrectness() {
        for (int i = 0; i < first35fibNumbers.length; i++) {
            Assertions.assertEquals(fibonacci.Recursive(i), first35fibNumbers[i]);
        }
    }

    @Test
    void testMemoizedCorrectness() {
        for (int i = 0; i < first35fibNumbers.length; i++) {
            Assertions.assertEquals(fibonacci.Memoized(i), first35fibNumbers[i]);
        }
    }

    @Test
    void testIterativeCorrectness() {
        for (int i = 0; i < first35fibNumbers.length; i++) {
            Assertions.assertEquals(fibonacci.Iterative(i), first35fibNumbers[i]);
        }
    }

    @Test
    void testNegativeInput() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> fibonacci.Recursive(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> fibonacci.Recursive(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> fibonacci.Recursive(-1));
    }
}
