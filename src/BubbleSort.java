/**
 * Created by yoon on 15. 4. 30..
 */
public class BubbleSort implements Sort {

    @Override
    public int[] sort(int[] arr) {

        int size = arr.length - 1;
        for (int i = size-1; i >= 0; i--) {
            for (int j = 0; j <= i-1; j++) {
                if (arr[j] > arr[j+1])
                    swap(arr, j, j+1);
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
