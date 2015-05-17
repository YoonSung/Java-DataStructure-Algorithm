package architree.datastructure.heap;

import architree.support.Util;

/**
 * Created by yoon on 15. 5. 17..
 */
public class BinaryMinHeap {
    private int[] data;
    private int heapSize;

    public BinaryMinHeap(int size) {
        data = new int[size];
        heapSize = 0;
    }

    public void insert(int value) {
        if (heapSize == data.length)
            throw new HeapException("Heap Overflow");
        else {
            heapSize++;
            data[heapSize - 1] = value;
            siftUp(heapSize - 1);
        }
    }

    public void removeMin() {
        if (isEmpty())
            throw new HeapException("Heap is Empty");
        else {
            data[0] = data[heapSize - 1];
            heapSize--;
            if (heapSize > 0)
                siftDown(0);
        }
    }

    private void siftUp(int nodeIndex) {
        if (nodeIndex != 0) {
            int parentIndex = getParentIndex(nodeIndex);
            if (data[parentIndex] > data[nodeIndex]) {
                Util.swap(data, parentIndex, nodeIndex);
                siftUp(parentIndex);
            }
        }
    }

    private void siftDown(int nodeIndex) {
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

    public int getMinimum() {
        if (isEmpty())
            throw new HeapException("Heap is Empty");
        else
            return data[0];
    }

    public boolean isEmpty() {
        return (heapSize == 0);
    }

    private int getLeftChildIndex(int nodeIndex) {
        return 2 * nodeIndex + 1;
    }

    private int getRightChildIndex(int nodeIndex) {
        return 2 * nodeIndex + 2;
    }

    private int getParentIndex(int nodeIndex) {
        return (nodeIndex - 1) / 2;
    }

    public class HeapException extends RuntimeException {
        public HeapException(String message) {
            super(message);
        }
    }
}
