import java.util.SortedMap;

/**
 * Created by yoon on 15. 4. 30..
 */
public class Main {

    public static void main(String[] args) {
        Sort algorithm = new BubbleSort();

        int[] array = new int[]{3,5,3,2,5,6,7,9};
        algorithm.sort(array);
    }
}
