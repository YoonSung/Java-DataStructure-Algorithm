package architree.datastructure.queue;

/**
 * Created by yoon on 15. 5. 18..
 */
public class PlainQueue implements Queue {

    private final int capacity;
    private int queueArray[];
    protected int frontIndex;
    protected int rearIndex;

    public PlainQueue(int capacity) {
        this.capacity = capacity;
        this.queueArray = new int[capacity];
        this.frontIndex = -1;
        this.rearIndex = -1;
    }

    public void enqueue(int data) {
        if (isFull())
            throw new QueueOverflowException();

        queueArray[++rearIndex] = data;
    }

    public int dequeue() {
        if (isEmpty())
            throw new QueueUnderflowException();

        return queueArray[++frontIndex];
    }

    public boolean isFull() {
        return rearIndex +1 == capacity;
    }

    public boolean isEmpty() {
        return frontIndex == rearIndex;
    }
}
