package architree.datastructure.tree.twothree;

/**
 * Created by yoon on 15. 7. 15..
 */
class TTNode<T extends Comparable> {
    private TTNode<T> parent;
    private TTNode<T> leftChild;
    private TTNode<T> rightChild;
    private TTNode<T> middleChild;

    // when node is 2-node, leftValue is the values, and rightValue is null
    private T leftValue;
    private T rightValue;

    // Flag for node type
    private boolean twoNode;

    protected TTNode() {}

    public static <T extends Comparable> TTNode<T> createTwoNode(T value) {
        TTNode<T> newNode = new TTNode<T>();
        newNode.leftValue = value;
        newNode.twoNode = true;
        return newNode;
    }

    public static <T extends Comparable> TTNode<T> createThreeNode(T leftValue, T rightValue) {
        TTNode<T> newNode = new TTNode<T>();
        newNode.leftValue = leftValue;
        newNode.rightValue = rightValue;

        return newNode;
    }

    public static HoleNode createHoleNode() {
        return new HoleNode();
    }

    public void replaceChild(TTNode currentChild, TTNode newChild) {
        if (currentChild == leftChild) {
            leftChild = newChild;
        } else if (currentChild == rightChild) {
            rightChild = newChild;
        } else {
            assert  middleChild == currentChild;
            middleChild = newChild;
        }
        newChild.setParent(this);
        currentChild.setParent(null);
    }

    public void setLeftChild(TTNode<T> leftChild) {
        this.leftChild = leftChild;

        if (leftChild != null)
            leftChild.setParent(this);
    }

    public void setRightChild(TTNode<T> rightChild) {
        this.rightChild = rightChild;
        if (rightChild != null)
            rightChild.setParent(this);
    }

    public void setMiddleChild(TTNode<T> middleChild) {
        this.middleChild = middleChild;
        if (middleChild != null)
            middleChild.setParent(this);
    }

    public TTNode<T> getLeftChild() {
        return leftChild;
    }

    public TTNode<T> getRightChild() {
        return rightChild;
    }

    public TTNode<T> getMiddleChild() {
        return middleChild;
    }

    public void removeChildren() {
        this.leftChild = null;
        this.rightChild = null;
    }

    public final void setParent(TTNode<T> parent) {
        this.parent = parent;
    }

    public final TTNode<T> getParent() {
        return parent;
    }

    public boolean isTerminal() {
        return leftChild == null && rightChild == null;
    }

    public T getValue() {
        assert isTwoNode();
        return leftValue;
    }

    public T getLeftValue() {
        assert isThreeNode();
        return leftValue;
    }

    public T getRightValue() {
        assert isThreeNode();
        return rightValue;
    }

    public boolean isThreeNode() {
        return !isTwoNode();
    }

    public boolean isTwoNode() {
        return twoNode;
    }


    public void setLeftValue(T leftValue) {
        this.leftValue = leftValue;
    }

    public void setRightValue(T rightValue) {
        this.rightValue = rightValue;
    }
}
