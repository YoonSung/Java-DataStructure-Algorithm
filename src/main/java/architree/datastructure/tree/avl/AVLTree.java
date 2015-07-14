package architree.datastructure.tree.avl;

/**
 * Created by yoon on 15. 7. 14..
 */
public class AVLTree {

    private AVLNode root;

    public AVLTree() {
        this.root = null;
    }

// [start] ------------- Public methods
    public void insert(Comparable element) {
        root = insert(element, root);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void inOrderTraverseTree() {
        if (isEmpty())
            System.out.println( "Empty tree" );
        else
            inOrderTraverseTree(root);
    }

    public Comparable findMin() {
        return elementAt(findMin(root));
    }

    public Comparable findMax() {
        return elementAt(findMax(root));
    }

    public Comparable find(Comparable node) {
        return elementAt(find(node, root));
    }
// [end] ------------- Public methods

// [start] ------------- main functions
    private AVLNode insert(Comparable element, AVLNode parentNode) {

        if (parentNode == null) {
            parentNode = new AVLNode(element, null, null);

        // Smaller than Parent
        } else if (element.compareTo(parentNode.getElement()) < 0) {

            //Recursively find and save location
            parentNode.setLeftChild(insert(element, parentNode.getLeftChild()));

            //Calculate Node's Balance Factor Value,
            //if imbalance occurred
            if (height(parentNode.getLeftChild()) - height(parentNode.getRightChild()) == 2) {
                if (element.compareTo(parentNode.getLeftChild().getElement()) < 0)
                    //LL Rotate
                    parentNode = rotateWithLeftChild(parentNode);
                else
                    //LR Rotate
                    parentNode = doubleRotateWithLeftChild(parentNode);
            }

        // Greater than Parent
        } else if (element.compareTo(parentNode.getElement()) > 0) {

            //Recursively find and save location
            parentNode.setRightChild(insert(element, parentNode.getRightChild()));

            //Calculate Node's Balance Factor Value,
            //if imbalance occurred
            if (height(parentNode.getLeftChild()) - height(parentNode.getRightChild()) == 2) {
                if (element.compareTo(parentNode.getRightChild().getElement()) > 0)
                    //RR Rotate
                    parentNode = rotateWithRightChild(parentNode);
                else
                    //RL Roat
                    parentNode = doubleRotateWithRightChild(parentNode);
            }
        }

        //Update Recursively balance Factor value from root
        parentNode.setBalanceFactor(getCurrentBalanceFactor(parentNode));

        return parentNode;
    }

    /**
     * <ROTATE RL>
     *
     * EX)
     *     6(parent)  LL      6           RR      5
     *       5        ->        5         ->    6   2
     *     2                      2
     */
    private AVLNode doubleRotateWithRightChild(AVLNode parentNode) {
        parentNode.setRightChild(rotateWithLeftChild(parentNode.getRightChild()));
        return rotateWithRightChild(parentNode);
    }


    /**
     * <ROTATE LR>
     *
     * EX)
     *     6(parent)  RR         6        LL      2
     *   5            ->       2          ->    5   6
     *     2                 5
     */
    private AVLNode doubleRotateWithLeftChild(AVLNode parentNode) {
        parentNode.setLeftChild(rotateWithRightChild(parentNode.getLeftChild()));
        return rotateWithLeftChild(parentNode);
    }

    /**
     * EX)
     *     6(parent)           5
     *       5        ->     6  2
     *         2
     */
    private AVLNode rotateWithRightChild(AVLNode parentNode) {
        AVLNode troubleMaker = parentNode.getRightChild();
        parentNode.setRightChild(troubleMaker.getLeftChild());
        troubleMaker.setLeftChild(parentNode);

        parentNode.setBalanceFactor(getCurrentBalanceFactor(parentNode));
        troubleMaker.setBalanceFactor(getCurrentBalanceFactor(troubleMaker));

        return troubleMaker;
    }

    /**
     * EX)
     *         6(parent)              5
     *       5             ->       2  6
     *     2
     */
    private AVLNode rotateWithLeftChild(AVLNode parentNode) {
        AVLNode troubleMaker = parentNode.getLeftChild();
        parentNode.setLeftChild(troubleMaker.getRightChild());
        troubleMaker.setRightChild(parentNode);

        parentNode.setBalanceFactor(getCurrentBalanceFactor(parentNode));
        troubleMaker.setBalanceFactor(getCurrentBalanceFactor(troubleMaker));

        return troubleMaker;
    }

// [end] ------------- main functions

    //+1 because null value height(null) return -1
    private static int getCurrentBalanceFactor(AVLNode node) {
        return max(height(node.getLeftChild()), height(node.getRightChild())) + 1;
    }

    private static int max(int leftHeightSize,int rightHeightSize) {
        return leftHeightSize > rightHeightSize ? leftHeightSize : rightHeightSize;
    }

    private static int height(AVLNode node)  {
        return node == null ? -1 : node.getBalanceFactor();
    }

    private Comparable elementAt(AVLNode node) {
        return node == null ? null : node.getElement();
    }

    private void inOrderTraverseTree(AVLNode node) {
        if (node != null) {
            inOrderTraverseTree(node.getLeftChild());
            System.out.println( node.getElement());
            inOrderTraverseTree(node.getRightChild());
        }
    }

    private AVLNode findMin(AVLNode node) {
        if (node == null)
            return node;

        while (node.getLeftChild() != null)
            node = node.getLeftChild();
        return node;
    }

    private AVLNode findMax(AVLNode node) {
        if (node == null)
            return node;

        while (node.getRightChild() != null)
            node = node.getRightChild();
        return node;
    }

    private AVLNode find(Comparable x, AVLNode node) {
        while (node != null)
            if (x.compareTo(node.getElement()) < 0)
                node = node.getLeftChild();
            else if (x.compareTo(node.getElement() ) > 0)
                node = node.getRightChild();
            else
                return node;    // Match

        return null;   // No match
    }
}
