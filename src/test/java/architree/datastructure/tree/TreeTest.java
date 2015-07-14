package architree.datastructure.tree;

import architree.datastructure.tree.avl.AVLTree;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by yoon on 15. 7. 13..
 */
public class TreeTest {

    @Test(expected = TreeException.class)
    public void BinaryTree_with_duplicated_data() {
        BinaryTree tree =  new BinaryTree();

        tree.add(new BNode(3, "AA"));
        tree.add(new BNode(3, "AA"));
    }

    @Test
    public void BinaryTreeTest () {
        BinaryTree tree =  new BinaryTree();
        Random random = new Random();

        final int MAX = 50;

        ArrayList<Integer> keyList = new ArrayList<Integer>(MAX);

        int count = 0;
        int currentNum = 0;
        while (count < 50) {
            currentNum = random.nextInt(99);
            System.out.println(currentNum);
            if (keyList.contains(currentNum))
                continue;
            else {
                keyList.add(currentNum);
                count++;
            }
        }

        char temp = 0;
        for (int i = 0; i < MAX; i++) {
            temp = (char) (65 +i);
            tree.add(new BNode(keyList.get(i), temp+""));
        }

        tree.inOrderTraverseTree();
    }

    @Test
    public void AVLTreeTest() {
        class TestElement implements Comparable<TestElement> {

            private final int value;

            public TestElement(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }

            public int compareTo(TestElement o) {
                return value < o.getValue() ?
                                            -1
                                            :
                                            value == o.getValue() ?
                                                                0
                                                                :
                                                                1;
            }

            @Override
            public String toString() {
                return "TestElement{" +
                        "value=" + value +
                        '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                TestElement that = (TestElement) o;

                return value == that.value;

            }

            @Override
            public int hashCode() {
                return value;
            }
        }

        AVLTree tree = new AVLTree();

        final int CREATE_ELEMENT_NUMBER = 4000;
        final int BETWEEN_ELEMENT_VALUE_GAP  =   50;
        for (int i = BETWEEN_ELEMENT_VALUE_GAP; i != 0; i = ( i + BETWEEN_ELEMENT_VALUE_GAP ) % CREATE_ELEMENT_NUMBER )
            tree.insert( new TestElement( i ) );

        if (((TestElement)(tree.findMin())).getValue() != 50
            || ((TestElement)(tree.findMax())).getValue() != CREATE_ELEMENT_NUMBER - BETWEEN_ELEMENT_VALUE_GAP )
                fail("Find Error!");

        for (int i = BETWEEN_ELEMENT_VALUE_GAP; i != 0; i = ( i + BETWEEN_ELEMENT_VALUE_GAP ) % CREATE_ELEMENT_NUMBER )
            if (((TestElement)(tree.find(new TestElement(i)))).getValue() != i)
                fail("Find Error!");

        tree.inOrderTraverseTree();
    }
}