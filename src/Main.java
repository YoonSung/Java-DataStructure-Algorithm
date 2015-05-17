import architree.datastructure.heap.BinaryMinHeap;
import architree.sort.QuickSort;
import architree.sort.Sort;

import java.util.Random;

/**
 * Created by yoon on 15. 4. 30..
 */
public class Main {

    public static void main(String[] args) {
        sort();
        tree();
    }

    private static void tree() {
        int size = 10;
        BinaryMinHeap heap = new BinaryMinHeap(size);

        for (int i = 0; i < size; i++) {
            heap.insert((int)(Math.random()* 99));
        }
    }

    private static void sort() {
        Sort algorithm = new QuickSort();

        int[] array = new int[]{9,8,7,6,5,4,3,2,1};

        printArray(array);
        algorithm.sort(array);
        printArray(array);
    }

    private static void printArray(int[] array) {
        System.out.print("[");
        for (int intValue : array) {
            System.out.print(intValue+" ");
        }
        System.out.println("]");
    }
}
