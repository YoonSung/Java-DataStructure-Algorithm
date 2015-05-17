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
        assertTrue(plainStack.isFull());

        try {
            pushAndCheck(plainStack, 6);
            fail("plainStack over flow exception expected");
        } catch(StackOverflowException e) {}

        popAndCheck(plainStack, 5);
        assertFalse(plainStack.isFull());

        popAndCheck(plainStack, 4);
        assertFalse(plainStack.isFull());

        popAndCheck(plainStack, 3);
        assertFalse(plainStack.isFull());

        popAndCheck(plainStack, 2);
        assertFalse(plainStack.isFull());

        popAndCheck(plainStack, 1);
        assertTrue(plainStack.isEmpty());

        try {
            popAndCheck(plainStack, 0);
            fail("plainStack under flow exception expected");
        } catch(StackUnderflowException e) {}
    }

    private void pushAndCheck(PlainStack plainStack, int value) {
        plainStack.push(value);
        assertEquals(value, plainStack.peek());
    }

    private void popAndCheck(PlainStack plainStack, int expectedValue) {
        assertEquals(expectedValue, plainStack.pop());
    }
}