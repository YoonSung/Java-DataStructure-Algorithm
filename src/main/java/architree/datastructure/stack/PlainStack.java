package architree.datastructure.stack;

/**
 * Created by yoon on 15. 5. 17..
 */
public class PlainStack implements Stack {

    private final int stackSize;
    private int[] stackArray;
    private int topIndex;

    public PlainStack(int stackSize) {
        this.stackSize = stackSize;
        this.stackArray = new int[stackSize];
        this.topIndex = -1;
    }

    public boolean isEmpty() {
        return topIndex == -1;
    }

    public boolean isFull() {
        return topIndex == stackSize-1;
    }


    public void push(int value) {
        if (isFull())
            throw new StackOverflowException();


        this.stackArray[++topIndex] = value;
    }

    public int peek() {
        return this.stackArray[topIndex];
    }

    public int pop() {
        if (isEmpty())
            throw new StackUnderflowException();

        return this.stackArray[topIndex--];
    }
}
