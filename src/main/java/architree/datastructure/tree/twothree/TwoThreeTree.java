package architree.datastructure.tree.twothree;

import javax.xml.soap.Node;

/**
 * Created by yoon on 15. 7. 15..
 */
public class TwoThreeTree<T extends Comparable> {

    enum CompareResult {
        SMALLER_THAN_PARENT,
        SAME_AS_PARENT,
        GREATER_THAN_PARENT
    }

    private CompareResult getCompareResult(TTNode<T> node, T child) {
        return getCompareResult(node.getValue(), child);
    }

    private CompareResult getLeftCompareResult(TTNode<T> node, T child) {
        return getCompareResult(node.getLeftChild(), child);
    }

    private CompareResult getRightCompareResult(TTNode<T> node, T child) {
        return getCompareResult(node.getRightChild(), child);
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
            if (node.isLeafNode()) {
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
                        threeNode.setLeftChild(result.getLeftChild());
                        threeNode.setMiddleChild(result.getRightChild());

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
            if (threeNode.isLeafNode()) {
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
                        returnValue.getLeftChild().setRightChild(threeNode.getRightChild());
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
}
