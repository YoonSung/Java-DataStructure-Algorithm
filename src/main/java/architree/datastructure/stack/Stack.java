package architree.datastructure.stack;

/**
 * Created by yoon on 15. 5. 18..
 */
public interface Stack {
    void push(int value);
    int peek();
    int pop();
    boolean isFull();
    boolean isEmpty();
}
