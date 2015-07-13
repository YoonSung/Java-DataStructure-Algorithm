package architree.datastructure.tree;

/**
 * Created by yoon on 15. 7. 13..
 */
public class BNode {
    private int key;
    private String value;
    private BNode leftChild, rightChild;

    public BNode(int key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "BNode{" +
                "key=" + key +
                ", value='" + value + '\'' +
                '}';
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public BNode getLeftChild() {
        return leftChild;
    }

    public BNode getRightChild() {
        return rightChild;
    }

    public void setLeftChild(BNode leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(BNode rightChild) {
        this.rightChild = rightChild;
    }
}
