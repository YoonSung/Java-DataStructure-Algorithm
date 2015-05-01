/**
 * Created by yoon on 15. 4. 30..
 */
public class QuickSort implements Sort {

    private int[] array;

    @Override
    public int[] sort(int[] arr) {
        this.array = arr;
        _sort(0, arr.length-1);
        return this.array;
    }

    private void _sort(int startIndex, int lastIndex) {

        if (startIndex < lastIndex) {
            int pivotIndex = partition(startIndex, lastIndex);
            _sort(startIndex, pivotIndex-1);
            _sort(pivotIndex+1, lastIndex);
        }
    }

    private int partition(int startIndex, int lastIndex) {

        int pivotIndex = startIndex;
        int pivotValue = this.array[pivotIndex];

        int leftPointIndex = pivotIndex;
        int rightPointIndex = lastIndex+1;

        while(leftPointIndex < rightPointIndex) {

            do
                rightPointIndex--;
            while(pivotValue < this.array[rightPointIndex] && leftPointIndex < rightPointIndex);


            do
                leftPointIndex++;
            while(pivotValue > this.array[leftPointIndex] && leftPointIndex < rightPointIndex);


            if (leftPointIndex < rightPointIndex) {
                Util.swap(this.array, leftPointIndex, rightPointIndex);

            }
        }

        Util.swap(this.array, pivotIndex, rightPointIndex);

        return rightPointIndex;
    }
}
