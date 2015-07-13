package architree.datastructure.tree;

/**
 * Created by yoon on 15. 7. 13..
 */
public class BinaryTree {
    BNode root;

    public void add(BNode newNode) {
        if (this.root == null) {
            root = newNode;
        } else {

            BNode currentNode = root;
            while (currentNode != null) {

                //Search LeftChild
                if (currentNode.getKey() > newNode.getKey()) {

                    if (currentNode.getLeftChild() == null) {
                        currentNode.setLeftChild(newNode);
                        break;
                    }
                    else
                        currentNode = currentNode.getLeftChild();

                //Search RightChild
                } else if (currentNode.getKey() < newNode.getKey()){

                    if (currentNode.getRightChild() == null) {
                        currentNode.setRightChild(newNode);
                        break;
                    }
                    else
                        currentNode = currentNode.getRightChild();


                } else if (currentNode.getKey() == newNode.getKey()){
                    throw new TreeException("Already Occupied");
                }
            }
        }
    }

    public BNode find(int key) {

        BNode currentNode = root;

        while (currentNode != null) {
            if (currentNode.getKey() > key) {
                currentNode = currentNode.getLeftChild();

            } else if (currentNode.getKey() < key) {
                currentNode = currentNode.getRightChild();

            } else {
                return currentNode;
            }
        }

        return null;
    }

    public BNode delete(int key) {

        BNode currentNode = root;
        BNode parentNode = null;

        //find current, parent node
        while (currentNode != null) {

            //smaller than currentNode
            if (currentNode.getKey() > key) {
                parentNode = currentNode;
                currentNode = currentNode.getLeftChild();

            //bigger than currentNode
            } else if (currentNode.getKey() < key) {
                parentNode = currentNode;
                currentNode = currentNode.getRightChild();

            //find!
            } else {
                break;
            }
        }

        //delete
        BNode targetNode = currentNode;

        if (targetNode == null)
            return null;

        BNode targetLeftChild = targetNode.getLeftChild();
        BNode targetRightChild = targetNode.getRightChild();

        //if Current Node is Leaf Node, just delete
        if (targetLeftChild == null && targetRightChild == null) {
            if (parentNode.getLeftChild() == targetNode)
                parentNode.setLeftChild(null);
            else
                parentNode.setRightChild(null);

        //if Current Node has Lef, right Both Side node
        } else if (targetLeftChild != null && targetRightChild != null) {
            if (parentNode.getLeftChild() == targetNode) {
                targetRightChild.setLeftChild(targetLeftChild);
                parentNode.setLeftChild(targetRightChild);
            } else {
                targetLeftChild.setRightChild(targetRightChild);
                parentNode.setRightChild(targetLeftChild);
            }

        //CurrentNode Has Only RightChild
        } else if (targetLeftChild == null && targetRightChild != null) {
           if (parentNode.getLeftChild() == targetNode)
               parentNode.setLeftChild(targetRightChild);
           else
               parentNode.setRightChild(targetRightChild);

        //CurrentNode Has Only LeftChild
        } else if (targetLeftChild != null && targetRightChild == null) {
            if (parentNode.getLeftChild() == targetNode)
                parentNode.setLeftChild(targetLeftChild);
            else
                parentNode.setRightChild(targetLeftChild);

        }

        targetNode.setLeftChild(null);
        targetNode.setRightChild(null);
        return targetNode;
    }

    public void inOrderTraverseTree() {
        inOrderTraverseTree(root);
    }

    public void inOrderTraverseTree(BNode node) {
        if (node == null)
            return;

        System.out.println(node.toString());
        inOrderTraverseTree(node.getLeftChild());
        inOrderTraverseTree(node.getRightChild());
    }

    public void preOrderTraverseTree() {
        preOrderTraverseTree(root);
    }

    public void preOrderTraverseTree(BNode node) {
        if (node == null)
            return;

        preOrderTraverseTree(node.getLeftChild());
        System.out.println(node.toString());
        preOrderTraverseTree(node.getRightChild());
    }

    public void postOrderTraverseTree() {
        postOrderTraverseTree(root);
    }

    public void postOrderTraverseTree(BNode node) {
        if (node == null)
            return;

        postOrderTraverseTree(node.getLeftChild());
        postOrderTraverseTree(node.getRightChild());
        System.out.println(node.toString());
    }
}
