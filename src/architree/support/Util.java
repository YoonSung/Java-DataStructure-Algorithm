package architree.support;

/**
 * Created by yoon on 15. 4. 30..
 */
public class Util {
    public static void swap(int[] arr, int j, int i) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
