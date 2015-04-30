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

            //divide
            _sort(startIndex, midIndex);
            _sort(midIndex + 1, lastIndex);

            //merge
            merge(startIndex, midIndex, lastIndex);
        }
    }

    private void merge(int startIndex, int midIndex, int lastIndex) {

        int leftPointIndex = startIndex;
        int rightPointIndex = midIndex+1;

        //+1 makes that both even and odd is successfully calculated
        int tempArraySize = lastIndex - startIndex+1;

        //create tempArray for ordered merge array
        int[] tempArray = new int[tempArraySize];
        int tempIndex = 0;

        while(rightPointIndex <= lastIndex && leftPointIndex <= midIndex) {

            if (this.array[leftPointIndex] < this.array[rightPointIndex])
                tempArray[tempIndex++] = this.array[leftPointIndex++];
            else
                tempArray[tempIndex++] = this.array[rightPointIndex++];
        }


        //left side values add (if leftPointIndex or rightPointIndex is move to max index, then opposite point must be added
        while(rightPointIndex <= lastIndex)
            tempArray[tempIndex++] = this.array[rightPointIndex++];

        while(leftPointIndex <= midIndex)
            tempArray[tempIndex++] = this.array[leftPointIndex++];

        for (int i = 0; i < tempArraySize; i++)
            this.array[startIndex + i] = tempArray[i];
    }
}
