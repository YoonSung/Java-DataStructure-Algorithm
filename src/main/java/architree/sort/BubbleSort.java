package architree.sort;

import architree.support.Util;

/**
 * Created by yoon on 15. 4. 30..
 */
public class BubbleSort implements Sort {

    @Override
    public int[] sort(int[] arr) {

        int size = arr.length;
        for (int i = 0; i < size; ++i) {
            for (int j = 1; j < size-i; ++j) {
                if (arr[j-1] > arr[j])
                    Util.swap(arr, j - 1, j);
            }
        }

        return arr;
    }
}
