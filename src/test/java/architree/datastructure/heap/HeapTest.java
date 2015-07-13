package architree.datastructure.heap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by yoon on 15. 5. 17..
 */
public class HeapTest {

    final int[] orderedArray = new int[]{0,1,2,3,4,5,6,7,8,9};
    int[] unorderedArray;

    @Before
    public void setUp() {
        unorderedArray = new int[]{9,8,7,6,5,4,3,2,1,0};
    }

    @Test
    public void binaryMinHeap() {

        final int minimumValue = 0;

        Heap heap = new BinaryMinHeap(unorderedArray.length);

        for (int i = 0; i < unorderedArray.length; i++) {
            heap.insert(unorderedArray[i]);
        }

        assertEquals(minimumValue, heap.peek());

        for (int i = 0 ; i <= 9 ; i++) {
            assertEquals(i, heap.remove());
        }

    }

    @Test
    public void binaryMaxHeap() {

        final int maximumValue = 9;

        Heap heap = new BinaryMaxHeap(unorderedArray.length);

        for (int i = 0; i < unorderedArray.length; i++) {
            heap.insert(unorderedArray[i]);
        }

        assertEquals(maximumValue, heap.peek());

        for (int i = 9 ; i <= 1 ; i--) {
            assertEquals(i, heap.remove());
        }

    }
}