/**
 * Created by yoon on 15. 4. 30..
 */
public class MergeSort implements Sort {


    private int[] array;

    @Override
    public int[] sort(int[] arr) {
        this.array = arr;
        _sort(0, arr.length - 1);

        return array;
    }

    private void _sort(int startIndex, int lastIndex) {

        if (startIndex < lastIndex) {
            int midIndex = startIndex + ((lastIndex - startIndex)/2);
            _sort(startIndex, midIndex);
            _sort(midIndex + 1, lastIndex);
            merge(startIndex, midIndex, lastIndex);
        }
    }

    private void merge(int startIndex, int midIndex, int lastIndex) {

        int leftPointIndex = startIndex;
        int rightPointIndex = midIndex+1;
        int tempArraySize = lastIndex - startIndex+1;

        int[] tempArray = new int[tempArraySize];
        int tempIndex = 0;

        while(rightPointIndex <= lastIndex && leftPointIndex <= midIndex) {

            int leftValue = this.array[leftPointIndex];
            int rightValue = this.array[rightPointIndex];

            if (leftValue < rightValue) {
                tempArray[tempIndex] = leftValue;
                leftPointIndex++;
                tempIndex++;

            } else {
                tempArray[tempIndex] = rightValue;
                rightPointIndex++;
                tempIndex++;
            }
        }

        while(rightPointIndex <= lastIndex) {
            tempArray[tempIndex] = this.array[rightPointIndex];

            rightPointIndex++;
            tempIndex++;
        }

        while(leftPointIndex <= midIndex) {
            tempArray[tempIndex] = this.array[leftPointIndex];

            leftPointIndex++;
            tempIndex++;
        }

        for (int i = 0; i < tempArraySize; i++) {
            this.array[startIndex + i] = tempArray[i];
        }
    }
}
