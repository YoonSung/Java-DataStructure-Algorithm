package architree.datastructure.tree.avl;

/**
 * Created by yoon on 15. 7. 14..
 */
public class AVLNode {

    private Comparable element;
    private AVLNode leftChild;
    private AVLNode rightChild;
    private int balanceFactor;

    AVLNode(Comparable element) {
        this(element, null, null);
    }

    public AVLNode(Comparable element, AVLNode leftChild, AVLNode rightChild) {
        this.element = element;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.balanceFactor = 0;
    }

    public Comparable getElement() {
        return element;
    }

    public AVLNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(AVLNode leftChild) {
        this.leftChild = leftChild;
    }

    public int getBalanceFactor() {
        return balanceFactor;
    }

    public AVLNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(AVLNode rightChild) {
        this.rightChild = rightChild;
    }

    public void setBalanceFactor(int balanceFactor) {
        this.balanceFactor = balanceFactor;
    }
}
