package architree.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoon on 15. 7. 13..
 */
public class RadixSort implements Sort {
    public int[] sort(int[] arr) {

        final int RADIX = 10;

        // Initialize Container Bucket
        List<Integer>[] bucket = new ArrayList[RADIX];
        for (int i = 0; i < bucket.length; i++) {
            //Create DIGIT Bucket
            bucket[i] = new ArrayList<Integer>();
        }

        // Sort
        // Flag to boundary condition
        boolean maxLength = false;
        int tmp = -1, placement = 1;
        while (!maxLength) {
            maxLength = true;
            // split input between lists
            for (Integer i : arr) {
                tmp = i / placement;

                //Into DIGIT Bucket
                bucket[tmp % RADIX].add(i);
                if (maxLength && tmp > 0) {
                    maxLength = false;
                }
            }
            // empty lists into input array
            int digitLocation = 0;
            for (int index = 0; index < RADIX; index++) {
                for (Integer i : bucket[index]) {
                    arr[digitLocation++] = i;
                }
                bucket[index].clear();
            }
            // move to next digit
            placement *= RADIX;
        }

        return arr;
    }
}
