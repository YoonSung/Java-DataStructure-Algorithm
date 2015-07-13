package architree.datastructure.tree;

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
}