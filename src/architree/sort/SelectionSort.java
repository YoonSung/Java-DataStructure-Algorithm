package architree.sort;

import architree.support.Util;

/**
 * Created by yoon on 15. 4. 30..
 */
public class SelectionSort implements Sort {
    @Override
    public int[] sort(int[] arr) {

        int size = arr.length;
        int maxValueIndex = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size-i; j++) {
                if (arr[j] > arr[maxValueIndex])
                    maxValueIndex = j;
            }
            Util.swap(arr, size - i - 1, maxValueIndex);
            maxValueIndex = 0;
        }

        return arr;
    }
}
