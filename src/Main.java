import java.util.SortedMap;

/**
 * Created by yoon on 15. 4. 30..
 */
public class Main {

    public static void main(String[] args) {
        Sort algorithm = new SelectionSort();

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
