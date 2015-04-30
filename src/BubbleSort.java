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
                    swap(arr, j-1, j);
            }
        }

        return arr;
    }

    private void swap(int[] arr, int j, int i) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
