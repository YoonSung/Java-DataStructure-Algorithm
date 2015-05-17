package architree.datastructure.queue;

/**
 * Created by yoon on 15. 5. 18..
 */
public interface Queue {
    void enqueue(int data);
    int dequeue();
    boolean isFull();
    boolean isEmpty();
}
