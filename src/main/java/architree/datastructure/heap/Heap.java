package architree.datastructure.heap;

/**
 * Created by yoon on 15. 7. 13..
 */
public abstract class Heap {
    protected int[] data;
    protected int heapSize;

    protected abstract void siftUp(int i);
    protected abstract void siftDown(int i);

    protected Heap(int size) {
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

    public int remove() {
        if (isEmpty())
            throw new HeapException("Heap is Empty");
        else {
            int temp = data[0];
            data[0] = data[heapSize - 1];
            heapSize--;
            if (heapSize > 0)
                siftDown(0);

            return temp;
        }
    }

    public int peek() {
        if (isEmpty())
            throw new HeapException("Heap is Empty");
        else
            return data[0];
    }

    public boolean isEmpty() {
        return (heapSize == 0);
    }

    protected int getLeftChildIndex(int nodeIndex) {
        return 2 * nodeIndex + 1;
    }

    protected int getRightChildIndex(int nodeIndex) {
        return 2 * nodeIndex + 2;
    }

    protected int getParentIndex(int nodeIndex) {
        return (nodeIndex - 1) / 2;
    }
}
