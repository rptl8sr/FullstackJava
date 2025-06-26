import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

public class CustomListTest {
    private CustomList<Object> customList;
    private ArrayList<Object> arrayList;

    @BeforeEach
    void init() {
        customList = new CustomList<>();
        arrayList = new ArrayList<>();
    }

    @Test
    void testAdd() {
        customList.add("First");
        customList.add("Second");
        arrayList.add("First");
        arrayList.add("Second");

        Assertions.assertEquals(customList.size(), arrayList.size());
    }

    @Test
    void testAddRandomIndex() {
        int size = 100;

        for (int i = 0; i < size; i++) {
            customList.add(i);
            arrayList.add(i);
        }

        for (int i = 0; i < 10; i++) {
            int randomNumber = (int) (Math.random() * (size + 1));

            customList.add(randomNumber, "New Element");
            arrayList.add(randomNumber, "New Element");
            Assertions.assertEquals(customList.size(), arrayList.size());
            Assertions.assertEquals(customList.get(size-1), arrayList.get(size-1));
        }
    }

    @Test
    void testGet() {
        customList.add("First");
        customList.add("Second");
        arrayList.add("First");
        arrayList.add("Second");

        Assertions.assertEquals(customList.get(0), arrayList.get(0));
        Assertions.assertEquals(customList.get(1), arrayList.get(1));
    }

    @Test
    void testGetOutOfIndex() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> customList.get(-1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> arrayList.get(-1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> customList.get(0));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> arrayList.get(0));
    }

    @Test
    void testSet() {
        customList.add("First");
        customList.add("Second");
        arrayList.add("First");
        arrayList.add("Second");

        Assertions.assertEquals(customList.set(0, "New First"), arrayList.set(0, "New First"));
        Assertions.assertEquals(customList.set(1, "New Second"), arrayList.set(1, "New Second"));
    }

    @Test
    void testSetOutOfIndex() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> customList.set(1, "New First"));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> arrayList.set(1, "New First"));
    }

    @Test
    void testRemove() {
        customList.add("First");
        customList.add("Second");
        arrayList.add("First");
        arrayList.add("Second");

        Assertions.assertEquals(customList.remove(1), arrayList.remove(1));
        Assertions.assertEquals(customList.remove(0), arrayList.remove(0));
    }

    @Test
    void testRemoveOutOfIndex() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> customList.remove(0));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> arrayList.remove(0));
    }

    @Test
    void testSizeDefaultCapacity() {
        for (int i = 0; i < 100; i++) {
            customList.add(i);
            arrayList.add(i);
            Assertions.assertEquals(customList.size(), arrayList.size());
        }

        Assertions.assertEquals(customList.get(99), arrayList.get(99));
    }

    @Test
    void testSizeCustomCapacity() {
        customList = new CustomList(1);
        for (int i = 0; i < 100; i++) {
            customList.add(i);
            arrayList.add(i);
            Assertions.assertEquals(customList.size(), arrayList.size());
        }

        Assertions.assertEquals(customList.get(99), arrayList.get(99));
    }

    @Test
    void testClear() {
        customList.add("First");
        customList.add("Second");
        arrayList.add("First");
        arrayList.add("Second");

        customList.clear();
        arrayList.clear();

        Assertions.assertEquals(customList.size(), arrayList.size());
        Assertions.assertTrue(customList.isEmpty());
        Assertions.assertTrue(arrayList.isEmpty());
    }

    @Test
    void testAddAndRemove() {
        int size = 100;

        for (int i = 0; i < size; i++) {
            customList.add(i);
            arrayList.add(i);
        }

        for (int i = size - 1; i >= 0; i--) {
            customList.remove(i);
            arrayList.remove(i);
            Assertions.assertEquals(customList.size(), arrayList.size());
        }
    }

    @Test
    void testAddAndRandomRemove() {
        int size = 100;

        for (int i = 0; i < size; i++) {
            customList.add(i);
            arrayList.add(i);
        }

        for (int i = size - 1; i >= 0; i--) {
            int randomNumber = (int) (Math.random() * (i + 1));

            customList.remove(randomNumber);
            arrayList.remove(randomNumber);
        }

        Assertions.assertEquals(customList.size(), arrayList.size());
    }

    @Test
    void testNullValues() {
        customList.add(null);
        customList.add("NotNull");
        customList.add(null);

        arrayList.add(null);
        arrayList.add("NotNull");
        arrayList.add(null);

        Assertions.assertEquals(customList.size(), arrayList.size());
        Assertions.assertEquals(customList.get(0), arrayList.get(0));
        Assertions.assertEquals(customList.get(2), arrayList.get(2));
        Assertions.assertNull(customList.get(0));
        Assertions.assertNull(arrayList.get(0));
    }

    @Test
    void testEmptyListIndex() {
        Assertions.assertTrue(customList.isEmpty());
        Assertions.assertTrue(arrayList.isEmpty());
        Assertions.assertEquals(customList.size(), arrayList.size());

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> customList.get(0));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> arrayList.get(0));
    }
}
