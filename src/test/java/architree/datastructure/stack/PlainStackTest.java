package architree.datastructure.stack;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by yoon on 15. 5. 17..
 */
public class PlainStackTest {

    @Test
    public void plainStack() {
        int stackSize = 5;
        PlainStack plainStack = new PlainStack(stackSize);

        pushTest(plainStack);
        assertTrue(plainStack.isFull());

        try {
            pushAndCheck(plainStack, 6);
            fail("plainStack over flow exception expected");
        } catch(StackOverflowException e) {}

        popTest(plainStack);
        assertTrue(plainStack.isEmpty());

        try {
            popAndCheck(plainStack, 0);
            fail("plainStack under flow exception expected");
        } catch(StackUnderflowException e) {}
    }

    @Test
    public void dynamicStack() {
        int stackSize = 5;
        DynamicStack dynamicStack = new DynamicStack(stackSize);

        pushTest(dynamicStack);
        assertTrue(dynamicStack.isFull());
        pushAndCheck(dynamicStack, 6);
        assertFalse(dynamicStack.isFull());

        pushAndCheck(dynamicStack, 7);
        assertFalse(dynamicStack.isFull());

        pushAndCheck(dynamicStack, 8);
        assertFalse(dynamicStack.isFull());

        pushAndCheck(dynamicStack, 9);
        assertFalse(dynamicStack.isFull());

        pushAndCheck(dynamicStack, 10);
        assertTrue(dynamicStack.isFull());
    }

    private void popTest(PlainStack plainStack) {
        popAndCheck(plainStack, 5);
        assertFalse(plainStack.isFull());

        popAndCheck(plainStack, 4);
        assertFalse(plainStack.isFull());

        popAndCheck(plainStack, 3);
        assertFalse(plainStack.isFull());

        popAndCheck(plainStack, 2);
        assertFalse(plainStack.isFull());

        popAndCheck(plainStack, 1);
    }

    private void pushTest(PlainStack plainStack) {
        assertTrue(plainStack.isEmpty());

        pushAndCheck(plainStack, 1);
        assertFalse(plainStack.isFull());

        pushAndCheck(plainStack, 2);
        assertFalse(plainStack.isFull());

        pushAndCheck(plainStack, 3);
        assertFalse(plainStack.isFull());

        pushAndCheck(plainStack, 4);
        assertFalse(plainStack.isFull());

        pushAndCheck(plainStack, 5);
    }

    private void pushAndCheck(PlainStack plainStack, int value) {
        plainStack.push(value);
        assertEquals(value, plainStack.peek());
    }

    private void popAndCheck(PlainStack plainStack, int expectedValue) {
        assertEquals(expectedValue, plainStack.pop());
    }
}