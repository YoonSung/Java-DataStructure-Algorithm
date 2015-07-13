package architree.sort;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * Created by yoon on 15. 5. 17..
 */
public class SortTest {

    int[] unorderedArray;
    int[] orderedArray;

    Sort algorithm;

    @Before
    public void setUp() throws Exception {
        orderedArray = new int[]{0,1,2,3,4,5,6,7,8,9};
        unorderedArray = new int[]{9,8,7,6,5,4,3,2,1,0};
    }

    @Test
    public void bubbleSort() {
        algorithm = new BubbleSort();
    }

    @Test
    public void selectionSort() {
        algorithm = new SelectionSort();
    }

    @Test
    public void mergeSort() {
        algorithm = new MergeSort();
    }

    @Test
    public void quickSort() {
        algorithm = new QuickSort();
    }

    @Test
    public void shellSort() { algorithm = new ShellSort(); }

    @Test
    public void radixSort() {
        unorderedArray = new int[]{50,4,55,10,20,15,60,99,74,0};
        orderedArray   = new int[]{0,4,10,15,20,50,55,60,74,99};
        algorithm = new RadixSort();
    }

    @Test
    public void countingSort() {
        unorderedArray = new int[]{3,3,2,3,1,1,0,0,2,0};
        orderedArray   = new int[]{0,0,0,1,1,2,2,3,3,3};
        algorithm = new CountingSort();
    }

    @After
    public void eachTestDo() {
        algorithm.sort(unorderedArray);
        assertTrue(Arrays.equals(unorderedArray, orderedArray));
    }

    /*
    @After
    public void endEachTest() {
        System.out.print("[");
        for (int intValue : unorderedArray) {
            System.out.print(intValue+" ");
        }
        System.out.println("]");
    }
    */
}