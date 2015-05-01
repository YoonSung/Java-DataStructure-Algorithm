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

    //Locate pivotValue to appropriate index
    private int partition(int startIndex, int lastIndex) {

        //Case ordered list, quicksort is ineffient performance.
        //Prevent that, pivot value is choosen randomly
        Util.swap(this.array, startIndex, startIndex + (lastIndex - startIndex)/2);

        int pivotIndex = startIndex;
        int pivotValue = this.array[pivotIndex];

        //Initial index is +1 and -1 Because do-while is automatically executed at least once.
        int leftPointIndex = pivotIndex;
        int rightPointIndex = lastIndex+1;

        while(leftPointIndex < rightPointIndex) {

            //Move rightPointer to the left until the value which is smaller than pivotValue is come out
            do
                rightPointIndex--;
            while(pivotValue < this.array[rightPointIndex] && leftPointIndex < rightPointIndex);

            //Same
            do
                leftPointIndex++;
            while(pivotValue > this.array[leftPointIndex] && leftPointIndex < rightPointIndex);


            if (leftPointIndex < rightPointIndex) {
                //Swap by pivotValue (great value is located at pivot'right, less value is located at pivot's left)
                Util.swap(this.array, leftPointIndex, rightPointIndex);
            }
        }

        Util.swap(this.array, pivotIndex, rightPointIndex);

        return rightPointIndex;
    }
}
