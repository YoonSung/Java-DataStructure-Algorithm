package architree.datastructure.queue;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by yoon on 15. 5. 18..
 */
public class QueueTest {
    @Test
    public void plainQueue() {
        PlainQueue queue = new PlainQueue(5);

        assertTrue(queue.isEmpty());
        queue.enqueue(1);
        assertFalse(queue.isEmpty());
        assertFalse(queue.isFull());

        queue.enqueue(2);
        assertFalse(queue.isFull());

        queue.enqueue(3);
        assertFalse(queue.isFull());

        queue.enqueue(4);
        assertFalse(queue.isFull());

        queue.enqueue(5);
        assertTrue(queue.isFull());

        try {
            queue.enqueue(6);
            fail("queue overflow exception expected");
        } catch (QueueOverflowException e){}

        assertEquals(1, queue.dequeue());

        //is problem
        assertTrue(queue.isFull());

        assertEquals(2, queue.dequeue());
        assertTrue(queue.isFull());

        assertEquals(3, queue.dequeue());
        assertTrue(queue.isFull());

        assertEquals(4, queue.dequeue());
        assertTrue(queue.isFull());

        assertEquals(5, queue.dequeue());
        assertTrue(queue.isFull());

        assertTrue(queue.isEmpty());

        try {
            queue.dequeue();
            fail("queue underflow exception expected");
        } catch (QueueUnderflowException e){}

        assertTrue(queue.isEmpty());
    }

    @Test
    public void circularQueue() {
        CircularQueue queue = new CircularQueue(5);

        assertTrue(queue.isEmpty());
        queue.enqueue(1);
        assertFalse(queue.isEmpty());
        assertFalse(queue.isFull());

        queue.enqueue(2);
        assertFalse(queue.isFull());

        queue.enqueue(3);
        assertFalse(queue.isFull());

        queue.enqueue(4);
        assertFalse(queue.isFull());

        queue.enqueue(5);
        assertTrue(queue.isFull());

        try {
            queue.enqueue(6);
            fail("queue overflow exception expected");
        } catch (QueueOverflowException e){}

        assertEquals(1, queue.dequeue());

        //plainQueue isFull() problem solved
        assertFalse(queue.isFull());

        assertEquals(2, queue.dequeue());

        assertEquals(3, queue.dequeue());

        assertEquals(4, queue.dequeue());

        assertEquals(5, queue.dequeue());

        assertTrue(queue.isEmpty());

        try {
            queue.dequeue();
            fail("queue underflow exception expected");
        } catch (QueueUnderflowException e){}

        assertTrue(queue.isEmpty());
    }
}