package architree.datastructure.stack;

/**
 * Created by yoon on 15. 5. 18..
 */
public class DynamicStack extends PlainStack {
    public DynamicStack(int stackSize) {
        super(stackSize);
    }

    @Override
    public void push(int value) {
        try {
            super.push(value);
        } catch (StackOverflowException e) {
            increaseStackCapacity();
            super.push(value);
        }

    }

    private void increaseStackCapacity(){
        int[] newStack = new int[this.stackSize*2];

        System.arraycopy(this.stackArray, 0, newStack, 0, stackSize);

        this.stackArray = newStack;
        this.stackSize = this.stackSize*2;
    }
}
