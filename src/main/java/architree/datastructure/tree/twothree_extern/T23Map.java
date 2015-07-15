package architree.datastructure.tree.twothree_extern;

/**
 * Created by yoon on 15. 7. 14..
 */
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

public class T23Map<K extends Comparable<? super K>,V> { // K : 키의 형태, V : 값의 형태
    ///////////////////////////////////////////////////////////////////////////
    // 일반적인 정의
    ///////////////////////////////////////////////////////////////////////////

    private class Pair { // 요소의 형태
        K key   = null;
        V value = null;

        Pair() {}
        Pair(K key, V x) { this.key = key; this.value = x; }
    }

    private class Node { // 노드의 형태
        int ways;        // 노드 종류 (활성 : -2, -1 분기 : 2, 3)
        Pair e1  = null; // 노드 요소 1
        Pair e2  = null; // 노드 요소 2
        Node lst = null; // 나무의 왼쪽 부분
        Node mst = null; // 나무의 일부
        Node rst = null; // 나무의 오른쪽 부분

        Node(int n)         { this.ways = n; }
        Node(int n, Node m) { this.ways = n; this.mst  = m; }
        Node(int n, Pair e) { this.ways = n; this.e1 = e; }

        Node(int n, Pair e, Node l, Node r) {
            this.ways = n;
            this.e1   = e;
            this.lst  = l;
            this.rst  = r;
        }

        Node(int n, Pair e1, Pair e2, Node l, Node m, Node r) {
            this.ways = n;
            this.e1   = e1;
            this.e2   = e2;
            this.lst  = l;
            this.mst  = m;
            this.rst  = r;
        }
    }
    private Node root = null; // root

    ///////////////////////////////////////////////////////////////////////////
    // insert(삽입)
    ///////////////////////////////////////////////////////////////////////////

    // 나무 root 키 key 값 x를 삽입
    public void insert(K key, V x) {
        root = insert(root, new Pair(key, x));
        root.ways = Math.abs(root.ways);
    }

    // 나무 t 엔트리 e 삽입
    private Node insert(Node t, Pair e) {
        if (t == null) return new Node(-2, e);
        int cmp1 = e.key.compareTo(t.e1.key), cmp2;
        System.out.println("key : "+ t.e1.key + ", way : "+t.ways);
        switch (t.ways) {
            case 2:
                if (cmp1 < 0) {
                    t.lst = insert(t.lst, e);
                    return balance2Li(t);
                }
                else if (cmp1 == 0) {
                    t.e1 = e;
                    return t;
                }
                else /* cmp1 > 0 */ {
                    t.rst = insert(t.rst, e);
                    return balance2Ri(t);
                }
            case 3:
                cmp2 = e.key.compareTo(t.e2.key);
                if (cmp1 < 0) {
                    t.lst = insert(t.lst, e);
                    return balance3Li(t);
                }
                else if (cmp1 == 0) {
                    t.e1 = e;
                    return t;
                }
                else if (cmp2 < 0) {
                    t.mst = insert(t.mst, e);
                    return balance3Mi(t);
                }
                else if (cmp2 == 0) {
                    t.e2 = e;
                    return t;
                }
                else /* cmp2 > 0 */ {
                    t.rst = insert(t.rst, e);
                    return balance3Ri(t);
                }
            default:
                buggy("insert");
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 삽입시의 밸런스 조정
    ///////////////////////////////////////////////////////////////////////////

    private Node balance2Li(Node t) {
        Node a = t.lst;
        if (a != null && a.ways == -2)
            return new Node(3, a.e1, t.e1, a.lst, a.rst, t.rst);
        return t;
    }

    /**
     *
     * parent가 binary tree로 구성되어 있을때,
     * parent에서 새로운 right child node가 생성될때 실행된다.
     *
     * 우선, 새로 요청되는 value가 parent element보다 더 크기 때문에
     * 오른쪽 child를 생성하고, 이 생성된 child값을 parent와 합치는 과정이다.
     *
     *
     *
     *         parentValue1 (Parent)                                         parentValue1, childValue1 (newNode)
     *  parentLeftChild | parentRightChild                              parentLeftChild, childLeftChild, childLeftRight
     *                                                      ->
     *                          childValue1 (Child)
     *                  childLeftChild   |  childRightChild
     */
    private Node balance2Ri(Node t) {
        Node a = t.rst;
        if (a != null && a.ways == -2)
            return new Node(3, t.e1, a.e1, t.lst, a.lst, a.rst);
        return t;
    }

    private Node balance3Li(Node t) {
        Node a = t.lst;
        if (a != null && a.ways == -2) {
            a.ways = -a.ways;
            return new Node(-2, t.e1, a, new Node(2, t.e2, t.mst, t.rst));
        }
        return t;
    }

    private Node balance3Mi(Node t) {
        Node a = t.mst;
        if (a != null && a.ways == -2) {
            a.lst = new Node(2, t.e1, t.lst, a.lst);
            a.rst = new Node(2, t.e2, a.rst, t.rst);
            return a;
        }
        return t;
    }

    /**
     * parent가 2개의 element를 가지고 있을경우, 오른쪽으로 insert하는 과정의 balance를 맞추는 함수이다.
     * parent에서 새로운 right child node 가 생성될때 실행된다.
     *
     * 우선, 새로 요청되는 value가 parent element보다 더 크기 때문에
     * 오른쪽 child를 생성하고, 이 생성된 child값을 parent와 합치는 과정이다.
     *
     *                 parentValue1, parentValue2                                         parentValue1, childValue1 (newNode)
     *  parentLeftChild | parentMiddleChild | parentRightChild                              parentLeftChild, childLeftChild, childLeftRight
     *                                                                  ->
     *                                          childValue1 (Child)
     *                                  childLeftChild   |  childRightChild
     */
    private Node balance3Ri(Node t) {
        Node a = t.rst;
        if (a != null && a.ways == -2) {
            a.ways = -a.ways;
            return new Node(-2, t.e2, new Node(2, t.e1, t.lst, t.mst), a);
        }
        return t;
    }

    ///////////////////////////////////////////////////////////////////////////
    // delete(삭제)
    ///////////////////////////////////////////////////////////////////////////

    // 나무 root에서 키 key 노드를 삭제
    public void delete(K key) {
        root = delete(root, key);
        if (root != null && root.ways == -1) root = root.mst;
    }

    // 나무 t에서 키 key 노드를 삭제
    private Node delete(Node t, K key) {
        if (t == null) return null;
        int cmp1 = key.compareTo(t.e1.key), cmp2;
        switch (t.ways) {
            case 2:
                if (cmp1 < 0) {
                    t.lst = delete(t.lst, key);
                    return balance2Ld(t);
                }
                else if (cmp1 == 0) {
                    if (t.lst == null) return new Node(-1); // 말단에서 제거
                    Pair lmax = new Pair();
                    t.lst = deleteMax(t.lst, lmax); // 내부에서 제거
                    t.e1 = lmax;
                    return balance2Ld(t);
                }
                else /* cmp1 > 0 */ {
                    t.rst = delete(t.rst, key);
                    return balance2Rd(t);
                }
            case 3:
                cmp2 = key.compareTo(t.e2.key);
                if (cmp1 < 0) {
                    t.lst = delete(t.lst, key);
                    return balance3Ld(t);
                }
                else if (cmp1 == 0) {
                    if (t.lst == null) return new Node(2, t.e2); // 말단에서 제거
                    Pair lmax = new Pair();
                    t.lst = deleteMax(t.lst, lmax); // 내부에서 제거
                    t.e1 = lmax;
                    return balance3Ld(t);
                }
                else if (cmp2 < 0) {
                    t.mst = delete(t.mst, key);
                    return balance3Md(t);
                }
                else if (cmp2 == 0) {
                    if (t.mst == null) return new Node(2, t.e1); // 말단에서 제거
                    Pair mmax = new Pair();
                    t.mst = deleteMax(t.mst, mmax); // 내부에서 제거
                    t.e2 = mmax;
                    return balance3Md(t);
                }
                else /* cmp2 > 0 */ {
                    t.rst = delete(t.rst, key);
                    return balance3Rd(t);
                }
            default:
                buggy("delete");
        }
        return null;
    }

    // 나무 t의 최대 키 노드를 삭제
    // 반환 값은 삭제하여 수정 된 나무
    // 나무 t 키의 최대 값과 그 값을 aux에 반환
    private Node deleteMax(Node t, Pair aux) {
        if (t.rst == null) {
            switch (t.ways) {
                case 2:
                    aux.key   = t.e1.key;
                    aux.value = t.e1.value;
                    return new Node(-1);
                case 3:
                    aux.key   = t.e2.key;
                    aux.value = t.e2.value;
                    return new Node(2, t.e1);
                default:
                    buggy("deleteMax");
            }
        }
        else {
            t.rst = deleteMax(t.rst, aux);
            switch (t.ways) {
                case 2:
                    return balance2Rd(t);
                case 3:
                    return balance3Rd(t);
                default:
                    buggy("deleteMax");
            }
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 삭제시 밸런스 조정
    ///////////////////////////////////////////////////////////////////////////

    private Node balance2Ld(Node t) {
        Node a = t.lst;
        if (a != null && a.ways == -1) {
            Node r = t.rst;
            switch (r.ways) {
                case 2:
                    Node mst = new Node(3, t.e1, r.e1, a.mst, r.lst, r.rst);
                    return new Node(-1, mst);
                case 3:
                    Node lst = new Node(2, t.e1, a.mst, r.lst);
                    Node rst = new Node(2, r.e2, r.mst, r.rst);
                    return new Node(2, r.e1, lst, rst);
                default:
                    buggy("balance2Ld");
            }
        }
        return t;
    }

    private Node balance2Rd(Node t) {
        Node a = t.rst;
        if (a != null && a.ways == -1) {
            Node l = t.lst;
            switch (l.ways) {
                case 2:
                    Node mst = new Node(3, l.e1, t.e1, l.lst, l.rst, a.mst);
                    return new Node(-1, mst);
                case 3:
                    Node rst = new Node(2, t.e1, l.rst, a.mst);
                    Node lst = new Node(2, l.e1, l.lst, l.mst);
                    return new Node(2, l.e2, lst, rst);
                default:
                    buggy("balance2Rd");
            }
        }
        return t;
    }

    private Node balance3Ld(Node t) {
        Node a = t.lst;
        if (a != null && a.ways == -1) {
            Node m = t.mst, r = t.rst, lst, mst;
            switch (m.ways) {
                case 2:
                    lst = new Node(3, t.e1, m.e1, a.mst, m.lst, m.rst);
                    return new Node(2, t.e2, lst, r);
                case 3:
                    lst = new Node(2, t.e1, a.mst, m.lst);
                    mst = new Node(2, m.e2, m.mst, m.rst);
                    return new Node(3, m.e1, t.e2, lst, mst, r);
                default:
                    buggy("balance3Ld");
            }
        }
        return t;
    }

    private Node balance3Md(Node t) {
        Node a = t.mst;
        if (a != null && a.ways == -1) {
            Node l = t.lst, r = t.rst, mst, rst;
            switch (r.ways) {
                case 2:
                    rst = new Node(3, t.e2, r.e1, a.mst, r.lst, r.rst);
                    return new Node(2, t.e1, l, rst);
                case 3:
                    mst = new Node(2, t.e2, a.mst, r.lst);
                    rst = new Node(2, r.e2, r.mst, r.rst);
                    return new Node(3, t.e1, r.e1, l, mst, rst);
                default:
                    buggy("balance3Md");
            }
        }
        return t;
    }

    private Node balance3Rd(Node t) {
        Node a = t.rst;
        if (a != null && a.ways == -1) {
            Node l = t.lst, m = t.mst, mst, rst;
            switch (m.ways) {
                case 2:
                    rst = new Node(3, m.e1, t.e2, m.lst, m.rst, a.mst);
                    return new Node(2, t.e1, l, rst);
                case 3:
                    rst = new Node(2, t.e2, m.rst, a.mst);
                    mst = new Node(2, m.e1, m.lst, m.mst);
                    return new Node(3, t.e1, m.e2, l, mst, rst);
                default:
                    buggy("balance3Rd");
            }
        }
        return t;
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////

    // 키를 검색합니다. 히트하면 true,해야 false
    public boolean member(K key) {
        Node t = root;
        while (t != null) {
            int cmp1 = key.compareTo(t.e1.key), cmp2;
            switch (t.ways) {
                case 2:
                    if      (cmp1 <  0) t = t.lst;
                    else if (cmp1 == 0) return true;
                    else t = t.rst;
                    break;
                case 3:
                    cmp2 = key.compareTo(t.e2.key);
                    if      (cmp1 <  0) t = t.lst;
                    else if (cmp1 == 0) return true;
                    else if (cmp2 <  0) t = t.mst;
                    else if (cmp2 == 0) return true;
                    else /* cmp2 > 0 */ t = t.rst;
                    break;
                default:
                    buggy("member");
            }
        }
        return false;
    }

    // 키 값을 얻는다. 키가 히트하지 않는 경우는 null
    public V lookup(K key) {
        Node t = root;
        while (t != null) {
            int cmp1 = key.compareTo(t.e1.key), cmp2;
            switch (t.ways) {
                case 2:
                    if      (cmp1 <  0) t = t.lst;
                    else if (cmp1 == 0) return t.e1.value;
                    else t = t.rst;
                    break;
                case 3:
                    cmp2 = key.compareTo(t.e2.key);
                    if      (cmp1 <  0) t = t.lst;
                    else if (cmp1 == 0) return t.e1.value;
                    else if (cmp2 <  0) t = t.mst;
                    else if (cmp2 == 0) return t.e2.value;
                    else /* cmp2 > 0 */ t = t.rst;
                    break;
                default:
                    buggy("lookup");
            }
        }
        return null;
    }

    // 맵이 빈 경우 true, 비어 있지 않으면 false
    public boolean isEmpty() { return root == null; }

    // 지도 비우기
    public void clear() { root = null; }

    // 키 목록
    public ArrayList<K> keys() {
        ArrayList<K> al = new ArrayList<K>();
        keys(root, al);
        return al;
    }

    // 값 목록
    public ArrayList<V> values() {
        ArrayList<V> al = new ArrayList<V>();
        values(root, al);
        return al;
    }

    // キーの最小値
    public K min() {
        Node t = root;
        if (t == null) return null;
        while (t.lst != null) t = t.lst;
        return t.e1.key;
    }

    // 키의 최대 값
    public K max() {
        Node t = root;
        if (t == null) return null;
        while (t.rst != null) t = t.rst;
        return t.ways == 2 ? t.e1.key : t.e2.key;
    }

    private void keys(Node t, ArrayList<K> al) {
        if (t != null) {
            switch (t.ways) {
                case 2:
                    keys(t.lst, al);
                    al.add(t.e1.key);
                    keys(t.rst, al);
                    break;
                case 3:
                    keys(t.lst, al);
                    al.add(t.e1.key);
                    keys(t.mst, al);
                    al.add(t.e2.key);
                    keys(t.rst, al);
                    break;
                default:
                    buggy("keys");
            }
        }
    }

    private void values(Node t, ArrayList<V> al) {
        if (t != null) {
            switch (t.ways) {
                case 2:
                    values(t.lst, al);
                    al.add(t.e1.value);
                    values(t.rst, al);
                    break;
                case 3:
                    values(t.lst, al);
                    al.add(t.e1.value);
                    values(t.mst, al);
                    al.add(t.e2.value);
                    values(t.rst, al);
                    break;
                default:
                    buggy("values");
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // debug용 루틴
    ///////////////////////////////////////////////////////////////////////////

    // 2-3트리를 그래프 문자열로 변환하기
    public String toString() { return toStr("", "", root); }

    // 2-3트리의 균형 여부 확인
    public boolean balanced() { return balanced(root); }

    // 아마 탐색 트리가 있는지 확인하기
    public boolean mstValid() { return mstValid(root); }

    private String toStr(String head, String bar, Node t) {
        if (t == null) return "";
        String str = "";
        switch (t.ways) {
            case 2:
                str += toStr(head + "　　", "／", t.rst);
                str += head + bar + t.e1.key + "\n";
                str += toStr(head + "　　", "＼", t.lst);
                break;
            case 3:
                String hb = head + bar;
                String hs = head + "　";
                String k1, k2;
                if (bar.equals("／")) {
                    k1 = hb + t.e1.key;
                    k2 = hs + t.e2.key;
                }
                else if (bar.equals("ー")) {
                    k1 = hs + t.e1.key;
                    k2 = hb + t.e2.key;
                }
                else if (bar.equals("＼")) {
                    k1 = hs + t.e1.key;
                    k2 = hb + t.e2.key;
                }
                else {
                    k1 = "" + t.e1.key;
                    k2 = "" + t.e2.key;
                }
                str += toStr(head + "　　", "／", t.rst);
                str += k2 + "\n";
                str += toStr(head + "　　", "ー", t.mst);
                str += k1 + "\n";
                str += toStr(head + "　　", "＼", t.lst);
                break;
            default:
                buggy("print");
        }
        return str;
    }

    private boolean balanced(Node t) {
        if (t == null) return true;
        boolean flag;
        switch (t.ways) {
            case 2:
                flag = height(t.lst) == height(t.rst);
                return flag && balanced(t.lst) && balanced(t.rst);
            case 3:
                int hl = height(t.lst);
                int hm = height(t.mst);
                int hr = height(t.rst);
                flag = hl == hm && hm == hr;
                flag = flag && balanced(t.lst);
                flag = flag && balanced(t.mst);
                flag = flag && balanced(t.rst);
                return flag;
            default:
                buggy("balanced");
        }
        return false;
    }

    // 나무의 높이를 반환
    private int height(Node t) {
        if (t == null) return 0;
        int sh = Math.max(height(t.lst), height(t.rst));
        if (t.ways == 2) return 1 + sh;
        return 1 + Math.max(sh, height(t.mst));
    }

    private boolean mstValid(Node t) {
        if (t == null) return true;
        boolean flag;
        switch (t.ways) {
            case 2:
                flag = small(t.e1.key, t.lst) && large(t.e1.key, t.rst);
                flag = flag && mstValid(t.lst) && mstValid(t.rst);
                return flag;
            case 3:
                flag = small(t.e1.key, t.lst);
                flag = flag && between(t.e1.key, t.e2.key, t.mst);
                flag = flag && large(t.e2.key, t.rst);
                flag = flag && mstValid(t.lst);
                flag = flag && mstValid(t.mst);
                flag = flag && mstValid(t.rst);
                return flag;
            default:
                buggy("mstValid");
        }
        return false;
    }

    private boolean small(K key, Node t) {
        if (t == null) return true;
        boolean flag = key.compareTo(t.e1.key) > 0;
        switch (t.ways) {
            case 2:
                flag = flag && small(key, t.lst) && small(key, t.rst);
                return flag;
            case 3:
                flag = flag && key.compareTo(t.e2.key) > 0;
                flag = flag && small(key, t.lst);
                flag = flag && small(key, t.mst);
                flag = flag && small(key, t.rst);
                return flag;
            default:
                buggy("small");
        }
        return false;
    }

    private boolean between(K key1, K key2, Node t) {
        if (t == null) return true;
        boolean flag = key1.compareTo(t.e1.key) < 0;
        flag = flag && key2.compareTo(t.e1.key) > 0;
        switch (t.ways) {
            case 2:
                flag = flag && between(key1, key2, t.lst);
                flag = flag && between(key1, key2, t.rst);
                return flag;
            case 3:
                flag = flag && key1.compareTo(t.e2.key) < 0;
                flag = flag && key2.compareTo(t.e2.key) > 0;
                flag = flag && between(key1, key2, t.lst);
                flag = flag && between(key1, key2, t.mst);
                flag = flag && between(key1, key2, t.rst);
                return flag;
            default:
                buggy("between");
        }
        return false;
    }

    private boolean large(K key, Node t) {
        if (t == null) return true;
        boolean flag = key.compareTo(t.e1.key) < 0;
        switch (t.ways) {
            case 2:
                flag = flag && large(key, t.lst) && large(key, t.rst);
                return flag;
            case 3:
                flag = flag && key.compareTo(t.e2.key) < 0;
                flag = flag && large(key, t.lst);
                flag = flag && large(key, t.mst);
                flag = flag && large(key, t.rst);
                return flag;
            default:
                buggy("large");
        }
        return false;
    }

    private static void buggy(String place) {
        System.err.println(place + ": This program is buggy");
        System.exit(1);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 메인 루틴
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        final int N = 100, M = 50;
        Random rnd = new Random();
        T23Map<Integer,Integer> m = new T23Map<Integer,Integer>();
        m.insert(1, 1);
        m.lookup(1);
        m.insert(5, 5);
        m.insert(7, 7);
        System.out.println(m);

        int[] r1 = new int[N];
        int[] r2 = new int[N];
        for (int i = 0; i < N; i++) r1[i] = rnd.nextInt(M);
        for (int i = 0; i < N; i++) r2[i] = rnd.nextInt(M) + M;
        for (int i = 0; i < N; i++) m.insert(r2[i], i);
        for (int i = 0; i < N; i++) m.insert(r1[i], i);
        for (int i = 0; i < N; i++) m.delete(r2[i]);
        int insertErrors = 0, deleteErrors = 0;
        for (int i = 0; i < N; i++) {
            if (!m.member(r1[i])) insertErrors++;
            if ( m.member(r2[i])) deleteErrors++;
        }
        System.out.println("【맵의 내부 구조】");
        System.out.println(m);
        System.out.print("【삽입】      ");
        System.out.println(insertErrors == 0 ? "OK" : "NG");
        System.out.print("【삭제】      ");
        System.out.println(deleteErrors == 0 ? "OK" : "NG");
        System.out.print("【균형】  ");
        System.out.println(m.balanced() ? "OK" : "NG");
        System.out.print("【아마 탐색 트리】");
        System.out.println(m.mstValid() ? "OK" : "NG");
        System.out.println();
        Arrays.sort(r1);
        System.out.println("【키 " + r1[0] + " 에 해당하는 값】");
        System.out.println("    " + m.lookup(r1[0]));
        System.out.println();
        System.out.println("【최소 키 값】");
        System.out.println("    " + m.min());
        System.out.println();
        System.out.println("【키의 최대 값】");
        System.out.println("    " + m.max());
        System.out.println();
        System.out.println("【키 목록】");
        System.out.println("    " + m.keys().size() + "갯수: " + m.keys());
        System.out.println();
        System.out.println("【값 목록】");
        System.out.println("    " + m.values().size() + "갯수: " + m.values());

    }
}