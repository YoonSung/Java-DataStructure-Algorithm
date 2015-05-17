package architree.datastructure.queue;

/**
 * Created by yoon on 15. 5. 18..
 */
public class CircularQueue implements Queue {

    private final int capacity;
    private int[] queueArray;
    private int frontIndex;
    private int rearIndex;

    public CircularQueue(int capacity) {
        this.capacity = capacity+1;
        this.queueArray = new int[capacity+1];

        this.frontIndex = 0;
        this.rearIndex = 0;
    }

    public void enqueue(int data) {
        if (isFull())
            throw new QueueOverflowException();

        rearIndex = (rearIndex + 1) % capacity;
        queueArray[rearIndex] = data;
    }

    public int dequeue() {
        if (isEmpty())
            throw new QueueUnderflowException();

        frontIndex = (frontIndex + 1) % capacity;
        return queueArray[frontIndex];
    }

    public boolean isFull() {
        return (rearIndex + 1) % capacity == frontIndex;
    }

    public boolean isEmpty() {
        return frontIndex == rearIndex;
    }
}
