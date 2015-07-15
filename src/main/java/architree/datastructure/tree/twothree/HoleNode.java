package architree.datastructure.tree.twothree;

/**
 * Created by yoon on 15. 7. 15..
 *
 * A hole node does not have any values, and have only one child.
 */
class HoleNode<T extends Comparable> extends TTNode<T> {
    private TTNode<T> child;

    HoleNode() {
        super();
    }

    @Override
    public boolean isTwoNode() {
        return false;
    }

    public TTNode getSibling() {
        if (getParent() != null) {
            return getParent().getLeftChild() == this ? getParent().getRightChild() : getParent().getLeftChild();
        }

        return null;
    }

    @Override
    public void setLeftChild(TTNode<T> leftChild) {}

    @Override
    public void removeChildren() {
        child = null;
    }

    @Override
    public void setRightChild(TTNode<T> rightChild) {}

    public TTNode<T> getChild() {
        return child;
    }

    public void setChild(TTNode<T> child) {
        this.child = child;
    }
}
