package architree.sort;

/**
 * Created by yoon on 15. 7. 13..
 * @see "http://blastic.tistory.com/127"
 */
public class ShellSort implements Sort {

    public int[] sort(int[] arr) {

        int inner, outer;
        int temp;

        int len = arr.length;

        //find initial value of h
        int h = 1;
        while (h <= len / 3)
            h = h * 3 + 1; // (1, 4, 13, 40, 121, ...)

        // decreasing h, until h=1
        while (h > 0)
        {
            for (outer = h; outer < len; outer++) {
                temp = arr[outer];
                inner = outer;

                //insertion sort
                while (inner >= h && arr[inner - h] >= temp) {
                    arr[inner] = arr[inner - h];
                    inner -= h;
                }

                arr[inner] = temp;
            }
            h = (h - 1) / 3; // decrease h
        }

        return arr;
    }
}
