package architree.datastructure.heap;

import architree.support.Util;

/**
 * Created by yoon on 15. 5. 17..
 */
public class BinaryMinHeap extends Heap {

    public BinaryMinHeap(int size) {
        super(size);
    }

    protected void siftUp(int nodeIndex) {
        if (nodeIndex != 0) {
            int parentIndex = getParentIndex(nodeIndex);
            if (data[parentIndex] > data[nodeIndex]) {
                Util.swap(data, parentIndex, nodeIndex);
                siftUp(parentIndex);
            }
        }
    }

    protected void siftDown(int nodeIndex) {
        int leftChildIndex = getLeftChildIndex(nodeIndex);
        int rightChildIndex = getRightChildIndex(nodeIndex);
        int minIndex;

        if (rightChildIndex >= heapSize) {
            //has no child
            if (leftChildIndex >= heapSize)
                return;

            //has only one leftChild
            else
                minIndex = leftChildIndex;

        //has complete child leaf
        } else {
            if (data[leftChildIndex] <= data[rightChildIndex])
                minIndex = leftChildIndex;
            else
                minIndex = rightChildIndex;
        }
        if (data[nodeIndex] > data[minIndex]) {
            Util.swap(data, minIndex, nodeIndex);
            siftDown(minIndex);
        }
    }
}
