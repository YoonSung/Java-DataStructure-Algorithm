package architree.sort;

/**
 * Created by yoon on 15. 7. 13..
 */
public class CountingSort implements Sort {

    public int[] sort(int[] arr) {
        int max = arr[0];
        int min = arr[0];

        for (int i = 0 ; i < arr.length ; i++) {
            if (arr[i] > max)
                max = arr[i];
            else if (arr[i] < min)
                min = arr[i];
        }

        int valueCount = max - min + 1;
        int [] countArray = new int[valueCount];
        for (int i = 0; i < arr.length; i++) {
            countArray[arr[i]-min]++;
        }

        int outputPosition = 0;
        for (int i =0 ; i < valueCount ; i++) {
            for (int j = 0 ; j < countArray[i] ; j++) {
                arr[outputPosition] = i+min;
                outputPosition++;
            }
        }

        return arr;
    }
}
