package architree.datastructure.tree.twothree;

/**
 * Created by yoon on 15. 7. 15..
 */
public class TwoThreeTree<T extends Comparable> {

    // [START] ------------ Public Method
    public boolean contains(T value) {
        return findNode(root, value) != null;
    }

    public T first() {
        TTNode<T> node = root;
        while (node.getLeftChild() != null) {
            node = node.getLeftChild();
        }
        return node.isThreeNode() ? node.getLeftValue() : node.getValue();
    }

    public T last() {
        TTNode<T> node = root;
        while (node.getRightChild() != null) {
            node = node.getRightChild();
        }
        return node.isThreeNode() ? node.getRightValue() : node.getValue();
    }

    public interface Function<T> {
        public void apply(T t);
    }


    /**
     * Preorder search.
     * Visit the node.
     * Visit the left subtree
     * Visit the middle subtree
     */
    public void preOrder(TTNode<T> node, Function<T> f) {
        if (node.isThreeNode()) {
            f.apply(node.getLeftValue());
            f.apply(node.getRightValue());
        }
        if (node.isTerminal())
            return;


        preOrder(node.getLeftChild(), f);
        if (node.isThreeNode()) {
            assert node.getMiddleChild() != null;
            preOrder(node.getMiddleChild(), f);
        }
        preOrder(node.getRightChild(), f);
    }



    public  void inorderSearch(TTNode<T> node, Function<T> func) {
        if (node == null)
            return;
        inorderSearch(node.getLeftChild(), func);
        if (node.isThreeNode()) {
            TTNode<T> threeNode = node;
            func.apply(threeNode.getLeftValue());
            inorderSearch(threeNode.getMiddleChild(), func);
            func.apply(threeNode.getRightValue());
        } else {
            func.apply(node.getValue());
        }
        inorderSearch(node.getRightChild(), func);
    }


    @Override
    public String toString() {
        if (size == 0)
            return "[]";
        final StringBuilder sb = new StringBuilder("[");
        inorderSearch(root, new Function<T>() {
            public void apply(T t) {
                sb.append(t);
                sb.append(", ");
            }
        });
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }
    // [END] ------------ Public Method



    enum CompareResult {
        SMALLER_THAN_PARENT,
        SAME_AS_PARENT,
        GREATER_THAN_PARENT
    }

    private CompareResult getCompareResult(TTNode<T> node, T child) {
        return getCompareResult(node.getValue(), child);
    }

    private CompareResult getLeftCompareResult(TTNode<T> node, T child) {
        return getCompareResult(node.getLeftValue(), child);
    }

    private CompareResult getRightCompareResult(TTNode<T> node, T child) {
        return getCompareResult(node.getRightValue(), child);
    }

    private CompareResult getCompareResult(T parent, T child) {
        int result = child.compareTo(parent);

        if (result > 0) {
            return CompareResult.GREATER_THAN_PARENT;
        } else if (result < 0) {
            return CompareResult.SMALLER_THAN_PARENT;
        } else {
            return CompareResult.SAME_AS_PARENT;
        }
    }



    private static final class DuplicateException extends RuntimeException {};
    private static final DuplicateException DUPLICATE = new DuplicateException();
    
    TTNode<T> root;
    int size = 0;

    public boolean add(T value) {
        if (root == null)
            root = TTNode.createTwoNode(value);
        else {
            try {
                TTNode<T> result = insert(value, root);

                if (result != null)
                    root = result;
            } catch (DuplicateException e) {
                return false;
            }
        }
        
        size ++;
        return true;
    }

    private TTNode<T> insert(T value, TTNode<T> node) throws DuplicateException {
        TTNode<T> returnValue = null;

        // ------------ parent가 2 node일 경우
        if (node.isTwoNode()) {
            CompareResult type = getCompareResult(node, value);

            /**
             * 만약 target node가 leaf 노드라면
             * 새로운 ThreeNode로 만들어서 바꿔치기 한다.
             *
             *    TwoNode(value1) + value2     ->   ThreeNode(value1, value2)
             *
             */
            if (node.isTerminal()) {
                if (type == CompareResult.SAME_AS_PARENT)
                    throw DUPLICATE;

                TTNode<T> threeNode = TTNode.createThreeNode(value, node.getValue());
                TTNode<T> parent = node.getParent();

                if (parent != null)
                    parent.replaceChild(node, threeNode);
                else
                    root = threeNode;

            // Not Leaf Node
            } else {
                /**
                 * Leaf node가 아니고, targetNode보다 작은값이라면
                 * targetNode의 leftNode에 데이터를 더하도록 재귀호출 한다
                 */
                if (type == CompareResult.SMALLER_THAN_PARENT) {
                    TTNode<T> result = insert(value, node.getLeftChild());


                    /**
                     * result Node가 어떻게 2Node임을 확신할 수 있는가? 의문
                     * 합쳐서 3노드를 만든다.
                     *
                     *
                     *             TwoNode(value1)
                     *             _   , rightChild1                  ThreeNode(value1, value2)
                     *             |                        ->         |           |          \
                     *        TwoNode(value2)                     leftChild2, rightChild2, rightChild1
                     *      leftChild2, rightChild2
                     */
                    if (result != null) {
                        TTNode threeNode = TTNode.createThreeNode(result.getValue(), node.getValue());
                        threeNode.setRightChild(node.getRightChild());
                        threeNode.setMiddleChild(result.getRightChild());
                        threeNode.setLeftChild(result.getLeftChild());

                        if (node.getParent() != null) {
                            node.getParent().replaceChild(node, threeNode);
                        } else {
                            root = threeNode;
                        }
                        unlinkNode(node);
                    }
                } else if (type == CompareResult.GREATER_THAN_PARENT) {
                    TTNode<T> result = insert(value, node.getRightChild());

                    /**
                     * result Node가 어떻게 2Node임을 확신할 수 있는가?
                     * leaf node가 3node가 되면 분할을 하기 때문
                     *
                     * Child노드와 합쳐서 3노드를 만든다.
                     *
                     *
                     *       TwoNode(value1)
                     *    leftChild1,      _                          ThreeNode(value1, value2)
                     *                      \              ->    leftChild1, leftChild2, rightChild2
                     *                  TwoNode(value2)
                     *               leftChild2, rightChild2
                     */
                    if (result != null) {
                        TTNode<T> threeNode = TTNode.createThreeNode(result.getValue(), node.getValue());
                        threeNode.setLeftChild(node.getLeftChild());
                        threeNode.setMiddleChild(result.getLeftChild());
                        threeNode.setRightChild(result.getRightChild());

                        if (node.getParent() != null) {
                            node.getParent().replaceChild(node, threeNode);
                        } else {
                            root = threeNode;
                        }
                        unlinkNode(node);
                    }
                } else {
                    throw DUPLICATE;
                }
            }

        // ------------ parent가 3 node일 경우
        } else {
            TTNode<T> threeNode = node;

            CompareResult leftType = getLeftCompareResult(threeNode, value);
            CompareResult rightType = getRightCompareResult(threeNode, value);

            if (leftType == CompareResult.SAME_AS_PARENT || rightType == CompareResult.SAME_AS_PARENT) {
                throw DUPLICATE;
            }

            /**
             * 단말노드일 경우,
             * 분할만 수행한다.
             */
            if (threeNode.isTerminal()) {
                returnValue = splitNode(threeNode, value);

            /**
             * 비단말 노드일 경우,
             * 재귀호출을 통해 데이터를 알맞은 위치에 더하고
             *
             * 새로 생성한 노드(2node)의 데이터의 값을
             * 원래 전달받은 노드(3node)에 더하면서 분할시킨다.
             * 리턴ㄷ
             */
            } else {
                /**
                 * Case: 새로운 value가 가장 작은값일 경우
                 *
                 *                     ThreeNode(=threeNode)(value1, value2)
                 *                        |            |             |
                 *      TwoNode(=result)(value3)      ?1            ?2
                 *          |          |
                 *      leftChild     rightChild
                 *
                 *
                 *    leftChild에 value를 재귀적으로 더한다.
                 *    -> result = insert(value, threeNode.leftChild());
                 *
                 *    return되는 2노드의 getValue()를 이용해서 split을 수행한다.
                 *
                 *
                 *                  TwoNode(=returnValue)(value(1|2|3))
                 *                   |                   \
                 *          TwoNode(value(1|2|3))         TwoNode(value(1|2|3))
                 *          |            |                   |        \
                 *      leftChild    rightChild             ?1        ?2
                 *
                 *
                 *   threeNode는 unlink 처리한다
                 *
                 * 즉, ThreeNode가 단말노드가 아니므로, 새로받은 value를 적절한 위치에 넣도록
                 * 왼쪽Child에게 위임한후, 하단에 새로운 노드가 생길것이기 때문에 높이를 맞추기 위해
                 * 현재 노드를 분할한다. 왼쪽 child는 2node이므로
                 * value를 가져와서 parent와 분할을 실시하고, 분할 결과로 생기는 2node들의 적절한 위치에
                 * 본래 데이터를 연결함으로서 높이조절을 성공한다.
                 *
                 */
                if (leftType == CompareResult.SMALLER_THAN_PARENT) {
                    TTNode<T> result = insert(value, threeNode.getLeftChild());

                    if (result != null) {
                        returnValue = splitNode(threeNode, result.getValue());
                        returnValue.getLeftChild().setLeftChild(result.getLeftChild());
                        returnValue.getLeftChild().setRightChild(result.getRightChild());
                        returnValue.getRightChild().setLeftChild(threeNode.getMiddleChild());
                        returnValue.getRightChild().setRightChild(threeNode.getRightChild());
                        unlinkNode(threeNode);
                    }

                /**
                 * Case: 새로운 value가 중간값일 경우
                 * 위와 동일하다. 위치만 바뀔 분
                 *
                 *                     ThreeNode(=threeNode)(value1, value2)
                 *                      |              |                  |
                 *                     ?1   TwoNode(=result)(value3)     ?2
                 *                                 |          |
                 *                          leftChild     rightChild
                 *
                 *
                 *    middleChild에 value를 재귀적으로 더한다.
                 *    -> result = insert(value, threeNode.middleChild());
                 *
                 *    return되는 2노드의 getValue()를 이용해서 split을 수행한다.
                 *
                 *
                 *                  TwoNode(=returnValue)(value(1|2|3))
                 *                   |                   \
                 *          TwoNode(value(1|2|3))         TwoNode(value(1|2|3))
                 *          |            |                   |           \
                 *         ?1       leftChild             rightChild     ?2
                 *
                 */
                } else if (rightType == CompareResult.SMALLER_THAN_PARENT){
                    TTNode<T> result = insert(value, threeNode.getMiddleChild());

                    if (result != null) {
                        returnValue = splitNode(threeNode, result.getValue());
                        returnValue.getLeftChild().setLeftChild(threeNode.getLeftChild());
                        returnValue.getLeftChild().setRightChild(result.getLeftChild());
                        returnValue.getRightChild().setLeftChild(result.getRightChild());
                        returnValue.getRightChild().setRightChild(threeNode.getRightChild());
                        unlinkNode(threeNode);
                    }
                /**
                 * Case: 새로운 value가 가장 큰 값일 경우
                 * 위와 동일하다. 위치만 바뀔 분
                 *
                 *                     ThreeNode(=threeNode)(value1, value2)
                 *                      |              |                  |
                 *                     ?1             ?2         TwoNode(=result)(value3)
                 *                                                    |          |
                 *                                              leftChild     rightChild
                 *
                 *
                 *    rightChild에 value를 재귀적으로 더한다.
                 *    -> result = insert(value, threeNode.rightChild());
                 *
                 *    return되는 2노드의 getValue()를 이용해서 split을 수행한다.
                 *
                 *
                 *                  TwoNode(=returnValue)(value(1|2|3))
                 *                   |                   \
                 *          TwoNode(value(1|2|3))         TwoNode(value(1|2|3))
                 *          |            |                   |           \
                 *         ?1           ?2             leftChild      rightChild
                 *
                 */
                } else {
                    TTNode<T> result = insert(value, threeNode.getRightChild());

                    if (result != null) {
                        returnValue = splitNode(threeNode, result.getValue());
                        returnValue.getLeftChild().setLeftChild(threeNode.getLeftChild());
                        returnValue.getLeftChild().setRightChild(threeNode.getMiddleChild());
                        returnValue.getRightChild().setLeftChild(result.getLeftChild());
                        returnValue.getRightChild().setRightChild(result.getRightChild());
                        unlinkNode(threeNode);
                    }
                }
            }
        }

        return returnValue;
    }

    private void unlinkNode(TTNode node) {
        node.removeChildren();
        node.setParent(null);
    }

    /**
     * 3Node에 새로운 Value를 더할경우, 분할을 한다.
     *
     *
     *       ThreeNode(value1 < value2)
     *    leftChild, middleChild, rightChild        + value3
     *
     *
     *    --> translate
     *
     *    * if value3 < value1
     *
     *          TwoNode(value1)
     *             |           \
     *    TwoNode(value3)    TwoNode(value2)
     *
     *
     *    * if value 1 < value3 < value 2
     *
     *          TwoNode(value3)
     *             |           \
     *    TwoNode(value1)    TwoNode(value2)
     *
     *
     *    * else
     *
     *          TwoNode(value2)
     *             |           \
     *    TwoNode(value1)    TwoNode(value3)
     *
     */
    private TTNode<T> splitNode(TTNode<T> threeNode, T value) {
        T min;
        T max;
        T middle;
        if (getLeftCompareResult(threeNode, value) == CompareResult.SMALLER_THAN_PARENT) {
            min = value;
            middle = threeNode.getLeftValue();
            max = threeNode.getRightValue();

        } else if (getRightCompareResult(threeNode, value) == CompareResult.SMALLER_THAN_PARENT) {
            min = threeNode.getLeftValue();
            middle = value;
            max = threeNode.getRightValue();

        } else {
            min = threeNode.getLeftValue();
            max = value;
            middle = threeNode.getRightValue();

        }

        TTNode<T> parent = TTNode.createTwoNode(middle);
        parent.setLeftChild(TTNode.createTwoNode(min));
        parent.setRightChild(TTNode.createTwoNode(max));

        return parent;
    }

    //http://cs.wellesley.edu/~cs230/spring07/2-3-trees.pdf
    public boolean remove(T value) {
        if (value == null)
            return false;

        TTNode<T> node = findNode(root, value);
        if (node == null)
            return false;

        HoleNode<T> hole = null;
        TTNode<T> terminalNode;
        T holeValue;
        
        if (node.isTerminal()) {
            terminalNode = node;
            holeValue = value;
            
        } else {

            if (node.isThreeNode()) {

                /**
                 * Replace by predecessor
                 *
                 * ex)
                 *
                 *                node  <- delete this
                 *                |
                 *            node
                 *            |  \
                 *        node   node   <- find, replace value
                 */
                if (node.getLeftValue().equals(value)) {
                    TTNode<T> predecessor = predecessor(node, value);
                    holeValue =  predecessor.isThreeNode() ? predecessor.getRightValue() : predecessor.getValue();
                    node.setLeftValue(holeValue);
                    terminalNode = predecessor;

                /**
                 * Replace by successor
                 *
                 * ex)
                 *
                 *    delete this -> node
                 *                       \
                 *                       node
                 *                       |   \
                 *      find this ->   node  node
                 *
                 *
                 * or is terminal node
                 *
                 *
                 * ex)
                 *
                 *
                 *       node     <- find this
                 *          \
                 *          node  <- delete this
                 *
                 */
                } else {
                    TTNode<T> successor = successor(node, value);
                    holeValue = successor.isThreeNode() ? successor.getLeftValue() : successor.getValue();
                    node.setRightValue(holeValue);
                    terminalNode = successor;
                }
                
                
            } else {
                TTNode<T> successor = successor(node, value);
                holeValue = successor.isThreeNode() ? successor.getLeftValue() : successor.getValue();
                node.setRightValue(holeValue);
                terminalNode = successor;
            }
        }
        
        assert terminalNode.isTerminal();

        if (terminalNode.isThreeNode()) {
            // Easy case. Replace 3-node by 2-node
            T remainedValue = terminalNode.getLeftValue().equals(holeValue) ? terminalNode.getRightValue() : terminalNode.getLeftValue();
            TTNode<T> twoNode = TTNode.createTwoNode(remainedValue);

            if (terminalNode.getParent() != null) {
                terminalNode.getParent().replaceChild(terminalNode, twoNode);
            } else {
                root = twoNode;
            }

        } else {
            if (terminalNode.getParent() != null) {
                /**
                 * To deal with a hole in a terminal 2-node,
                 * we consider it to be a special hole node that has a single subtree.
                 *
                 * For the purposes of calculating heights,
                 * such a hole node does contribute to the height of the tree.
                 * This decision allows the the path-length invariant
                 * to be preserved in trees with holes.
                 */
                hole = TTNode.createHoleNode();
                terminalNode.getParent().replaceChild(terminalNode, hole);
            } else {
                root = null;
            }
        }

        /**
         * @see 2-3Tree Deletion : Upward Phase in "http://cs.wellesley.edu/~cs230/spring07/2-3-trees.pdf"
         */
        while (hole != null) {
            /**
             * Case 1. The hole has a 2-node as parent and 2-node as sibling.
             *
             *           2Node(value1)
             *           |           \                                                3Node(value1, value2)
             *        Hole         2Node(value2)               ->                    |         |          \
             *         |            |           \                           2Node(value3)  2Node(value4)  2Node(value5)
             *     2Node(value3)  2Node(value4)  2Node(value5)
             */
            if (hole.getParent().isTwoNode() && hole.getSibling().isTwoNode()) {
                TTNode<T> parent = hole.getParent();
                TTNode<T> sibling = hole.getSibling();

                TTNode<T> threeNode = TTNode.createThreeNode(parent.getValue(), sibling.getValue());

                if (parent.getLeftChild() == hole) {
                    threeNode.setLeftChild(hole.getChild());
                    threeNode.setMiddleChild(sibling.getLeftChild());
                    threeNode.setRightChild(sibling.getRightChild());

                } else {
                    threeNode.setLeftChild(sibling.getLeftChild());
                    threeNode.setMiddleChild(sibling.getRightChild());
                    threeNode.setRightChild(hole.getChild());
                }


                if (parent.getParent() == null) {
                    unlinkNode(hole);
                    root = threeNode;
                    hole = null;

                } else {
                    hole.setChild(threeNode);
                    parent.getParent().replaceChild(parent, hole);
                }

                unlinkNode(parent);
                unlinkNode(sibling);
            }

            /**
             * Case 2. The hole has a 2-node as parent and 3-node as sibling.
             *
             *           2Node(value1)                                                                       2Node(value2)
             *           |            \                                                                      |           \
             *        Hole            3Node(value2,     value3)                 ->                 2Node(value1)         2Node(value3)
             *         |              |           \           \                                    |          \          |           \
             *     2Node(value4)  2Node(value5) 2Node(value6)  2Node(value7)             2Node(value4)  2Node(value5) 2Node(value6)  2Node(value7)
             */
            else if (hole.getParent().isTwoNode() && hole.getSibling().isThreeNode()) {
                TTNode<T> parent = hole.getParent();
                TTNode<T> sibling = hole.getSibling();

                if (parent.getLeftChild() == hole) {
                    TTNode<T> leftChild = TTNode.createTwoNode(parent.getValue());
                    TTNode<T> rightChild = TTNode.createTwoNode(sibling.getRightValue());

                    parent.setLeftValue(sibling.getLeftValue());
                    parent.replaceChild(hole, leftChild);
                    parent.replaceChild(sibling, rightChild);

                    leftChild.setLeftChild(hole.getChild());
                    leftChild.setRightChild(sibling.getLeftChild());

                    rightChild.setLeftChild(sibling.getMiddleChild());
                    rightChild.setRightChild(sibling.getRightChild());

                } else {
                    TTNode<T> leftChild = TTNode.createTwoNode(sibling.getLeftValue());
                    TTNode<T> rightChild = TTNode.createTwoNode(parent.getValue());

                    parent.setLeftValue(sibling.getRightValue());
                    parent.replaceChild(sibling, leftChild);
                    parent.replaceChild(hole, rightChild);

                    leftChild.setLeftChild(sibling.getLeftChild());
                    leftChild.setRightChild(sibling.getMiddleChild());

                    rightChild.setLeftChild(sibling.getRightChild());
                    rightChild.setRightChild(hole.getChild());
                }

                unlinkNode(hole);
                unlinkNode(sibling);
                hole = null;

            /**
             * Case 3. The hole has a 3-node as parent and 2-node as sibling.
             *
             *
             *    subcase (a) -- @see more in "https://twothreetree.googlecode.com/svn-history/r2/trunk/src/sergey/melderis/twothreetree/TwoThreeTree.java"
             *
             *                  3Node(value1, value2)                                                2Node(value2)
             *                  |         |         \                                                |           \
             *               Hole     2Node(value3)  2Node(value4)     ->        3Node(value1, value3)         2Node(value4)
             *               |        |           \                                |        |        \
             *    2Node(value5)  2Node(value6)    2Node(value7)         2Node(value5)  2Node(value6) 2Node(value7)
             *
             *
             *           or
             *
             *
             *   subcase (b)
             *
             *                  3Node(value1, value2)                                                2Node(value1)
             *                  |         |         \                                                |           \
             *            2Node(value3)  Hole       2Node(value4)     ->                  2Node(value3)         3Node(value2, value4)
             *                            |         |           \                                                |        |        \
             *                    2Node(value5)  2Node(value6)  2Node(value7)                         2Node(value5)  2Node(value6) 2Node(value7)
             *
             *
             */
            } else if (hole.getParent().isThreeNode()) {
                TTNode<T> parent = hole.getParent();

                // subcase (a), hole is in the middle
                // @see https://twothreetree.googlecode.com/svn-history/r2/trunk/src/sergey/melderis/twothreetree/TwoThreeTree.java
                if (parent.getMiddleChild() == hole && parent.getLeftChild().isTwoNode()) {
                    TTNode<T> leftChild = parent.getLeftChild();
                    TTNode<T> newParent = TTNode.createTwoNode(parent.getRightValue());
                    TTNode<T> newLeftChild = TTNode.createThreeNode(leftChild.getValue(), parent.getLeftValue());
                    newParent.setLeftChild(newLeftChild);
                    newParent.setRightChild(parent.getRightChild());
                    if (parent != root) {
                        parent.getParent().replaceChild(parent, newParent);
                    } else {
                        root = newParent;
                    }

                    newLeftChild.setLeftChild(leftChild.getLeftChild());
                    newLeftChild.setMiddleChild(leftChild.getRightChild());
                    newLeftChild.setRightChild(hole.getChild());

                    unlinkNode(parent);
                    unlinkNode(leftChild);
                    unlinkNode(hole);
                    hole = null;

                    // subcase (b), hole is in the middle
                } else if (parent.getMiddleChild() == hole && parent.getRightChild().isTwoNode()) {
                    TTNode<T> rightChild = parent.getRightChild();
                    TTNode<T> newParent = TTNode.createTwoNode(parent.getLeftValue());
                    TTNode<T> newRightChild = TTNode.createThreeNode(parent.getRightValue(), rightChild.getValue());
                    newParent.setLeftChild(parent.getLeftChild());
                    newParent.setRightChild(newRightChild);
                    if (parent != root) {
                        parent.getParent().replaceChild(parent, newParent);
                    } else {
                        root = newParent;
                    }
                    newRightChild.setLeftChild(hole.getChild());
                    newRightChild.setMiddleChild(rightChild.getLeftChild());
                    newRightChild.setRightChild(rightChild.getRightChild());
                    unlinkNode(parent);
                    unlinkNode(rightChild);
                    unlinkNode(hole);
                    hole = null;
                } else if (parent.getMiddleChild().isTwoNode()) {
                    TTNode<T> middleChild = parent.getMiddleChild();

                    // subcase (a). hole is the left child.
                    if (parent.getLeftChild() == hole) {
                        TTNode<T> newParent = TTNode.createTwoNode(parent.getRightValue());
                        TTNode<T> leftChild = TTNode.createThreeNode(parent.getLeftValue(), middleChild.getValue());
                        newParent.setLeftChild(leftChild);
                        newParent.setRightChild(parent.getRightChild());
                        if (parent != root) {
                            parent.getParent().replaceChild(parent, newParent);
                        } else {
                            root = newParent;
                        }

                        leftChild.setLeftChild(hole.getChild());
                        leftChild.setMiddleChild(middleChild.getLeftChild());
                        leftChild.setRightChild(middleChild.getRightChild());

                        unlinkNode(parent);
                        unlinkNode(hole);
                        unlinkNode(middleChild);
                        hole = null;

                        // subcase (a). hole is the right child.
                    } else if (parent.getRightChild() == hole) {
                        //System.out.println("Case 3 (a) hole is right child");
                        TTNode<T> newParent = TTNode.createTwoNode(parent.getLeftValue());
                        TTNode<T> rightChild = TTNode.createThreeNode(middleChild.getValue(), parent.getRightValue());
                        newParent.setRightChild(rightChild);
                        newParent.setLeftChild(parent.getLeftChild());
                        if (parent != root) {
                            parent.getParent().replaceChild(parent, newParent);
                        } else {
                            root = newParent;
                        }

                        rightChild.setLeftChild(middleChild.getLeftChild());
                        rightChild.setMiddleChild(middleChild.getRightChild());
                        rightChild.setRightChild(hole.getChild());

                        unlinkNode(parent);
                        unlinkNode(hole);
                        unlinkNode(middleChild);
                        hole = null;
                    }

                /**
                 * Case 4. The hole has a 3-node as parent and 3-node as sibling.
                 *    subcase (a) -- @see more in "https://twothreetree.googlecode.com/svn-history/r2/trunk/src/sergey/melderis/twothreetree/TwoThreeTree.java"
                 *    subcase (b)
                 */
                } else if (parent.getMiddleChild().isThreeNode()) {
                    TTNode<T> middleChild = parent.getMiddleChild();
                    // subcase (a) hole is the left child
                    if (hole == parent.getLeftChild()) {
                        TTNode<T> newLeftChild = TTNode.createTwoNode(parent.getLeftValue());
                        TTNode<T> newMiddleChild = TTNode.createTwoNode(middleChild.getRightValue());
                        parent.setLeftValue(middleChild.getLeftValue());
                        parent.setLeftChild(newLeftChild);
                        parent.setMiddleChild(newMiddleChild);
                        newLeftChild.setLeftChild(hole.getChild());
                        newLeftChild.setRightChild(middleChild.getLeftChild());
                        newMiddleChild.setLeftChild(middleChild.getMiddleChild());
                        newMiddleChild.setRightChild(middleChild.getRightChild());

                        unlinkNode(hole);
                        unlinkNode(middleChild);
                        hole = null;
                    }
                    // subcase (b) hole is the right child
                    else if (hole == parent.getRightChild()) {
                        // System.out.println("Case 4 (b) hole is right child");
                        TTNode<T> newMiddleChild = TTNode.createTwoNode(middleChild.getLeftValue());
                        TTNode<T> newRightChild = TTNode.createTwoNode(parent.getRightValue());
                        parent.setRightValue(middleChild.getRightValue());
                        parent.setMiddleChild(newMiddleChild);
                        parent.setRightChild(newRightChild);
                        newMiddleChild.setLeftChild(middleChild.getLeftChild());
                        newMiddleChild.setRightChild(middleChild.getMiddleChild());
                        // newMiddleChild.setParent(middleChild.middleChild());
                        newRightChild.setLeftChild(middleChild.getRightChild());
                        newRightChild.setRightChild(hole.getChild());

                        unlinkNode(hole);
                        unlinkNode(middleChild);
                        hole = null;

                    } else if (hole == parent.getMiddleChild() && parent.getLeftChild().isThreeNode()) {
                        // System.out.println("Case 4 (a) hole is middle child, left is 3-node");
                        TTNode<T> leftChild = parent.getLeftChild();
                        TTNode<T> newLeftChild = TTNode.createTwoNode(leftChild.getLeftValue());
                        TTNode<T> newMiddleChild = TTNode.createTwoNode(parent.getLeftValue());
                        parent.setLeftValue(leftChild.getRightValue());
                        parent.setLeftChild(newLeftChild);
                        parent.setMiddleChild(newMiddleChild);
                        newLeftChild.setLeftChild(leftChild.getLeftChild());
                        newLeftChild.setRightChild(leftChild.getMiddleChild());
                        newMiddleChild.setLeftChild(leftChild.getRightChild());
                        newMiddleChild.setRightChild(hole.getChild());

                        unlinkNode(hole);
                        unlinkNode(leftChild);
                        hole = null;
                    } else {
                        assert (hole == parent.getMiddleChild() && parent.getRightChild().isThreeNode());
                        // System.out.println("Case 4 (b) hole is middle child, right is 3-node");
                        TTNode<T> rightChild = parent.getRightChild();
                        TTNode<T> newRightChild = TTNode.createTwoNode(rightChild.getRightValue());
                        TTNode<T> newMiddleChild = TTNode.createTwoNode(parent.getRightValue());
                        parent.setRightValue(rightChild.getLeftValue());
                        parent.setMiddleChild(newMiddleChild);
                        parent.setRightChild(newRightChild);
                        newRightChild.setRightChild(rightChild.getRightChild());
                        newRightChild.setLeftChild(rightChild.getMiddleChild());
                        newMiddleChild.setRightChild(rightChild.getLeftChild());
                        newMiddleChild.setLeftChild(hole.getChild());

                        unlinkNode(hole);
                        unlinkNode(rightChild);
                        hole = null;
                    }
                }
            }
        }

        size--;
        return true;
    }

    private TTNode<T> successor(TTNode<T> node, T value) {
        if (node == null)
            return null;

        if (!node.isTerminal()) {
            TTNode<T> successor;
            if (node.isThreeNode() && node.getLeftValue().equals(value))
                successor = node.getMiddleChild();
            else
                successor = node.getRightChild();

            while (successor.getLeftChild() != null)
                successor = successor.getLeftChild();

            return successor;
        } else {
            TTNode<T> parent = node.getParent();

            if (parent == null)
                return null;

            TTNode<T> child = node;
            while (parent != null && child == parent.getRightChild()) {
                child = parent;
                parent = parent.getParent();
            }

            return parent != null ? parent : null;
        }
    }

    private TTNode<T> predecessor(TTNode<T> node, T value) {
        if (node == null)
            return null;

        TTNode<T> predecessor;

        if (!node.isTerminal()) {
            if (node.isThreeNode() && node.getRightValue().equals(value))
                predecessor = node.getMiddleChild();
            else
                predecessor = node.getLeftChild();

            while (predecessor.getRightChild() != null)
                predecessor = predecessor.getRightChild();

            return predecessor;
        } else {
            throw new UnsupportedOperationException("Implement predecessor parent is not terminal node");
        }
    }

    private TTNode<T> findNode(TTNode<T> node, T value) {
        if (node == null)
            return null;

        // 3 Node
        if (node.isThreeNode()) {
            CompareResult leftType = getLeftCompareResult(node, value);
            CompareResult rightType = getRightCompareResult(node, value);

            if (leftType == CompareResult.SAME_AS_PARENT || rightType == CompareResult.SAME_AS_PARENT) {
                return node;
            }

            if (leftType == CompareResult.SMALLER_THAN_PARENT)
                return findNode(node.getLeftChild(), value);

            else if (rightType == CompareResult.SMALLER_THAN_PARENT)
                return findNode(node.getMiddleChild(), value);
            //Greater than Parent
            else
                return findNode(node.getRightChild(), value);

        // 2 Node
        } else {
            CompareResult type = getCompareResult(node, value);

            if (type == CompareResult.SAME_AS_PARENT)
                return node;
            else if (type == CompareResult.SMALLER_THAN_PARENT)
                return findNode(node.getLeftChild(), value);
            else
                return findNode(node.getRightChild(), value);
        }
    }
}
//@see more https://twothreetree.googlecode.com/svn-history/r2/trunk/src/sergey/melderis/twothreetree/TwoThreeTree.java