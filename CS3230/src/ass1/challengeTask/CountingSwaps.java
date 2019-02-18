package ass1.challengeTask;

import java.io.*;
import java.util.*;

class CountingSwaps {

    // count number of swaps using divide and conquer
    private static long countSwaps(int L, int R, int[] W, int[] id, int[] temp, int D) {
        // if only one element, no swaps needed
        if (L == R) return 0;

        // split array into left and right subarray
        // recurse into each subarray
        int M = (L + R) / 2;
        long swaps = 0;
        int[] tempW = new int[temp.length];
        swaps += countSwaps(L, M, W, id, temp, D);
        swaps += countSwaps(M + 1, R, W, id, temp, D);

        TreeSet<Node> set = new TreeSet<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if(o1.weight - o2.weight != 0) {
                    return o1.weight - o2.weight;
                } else {
                    return o1.id - o2.id;
                }
            }
        });

        for(int i = 0; i < M - L + 1; i++) {
            set.add(new Node(id[L + i], W[L + i] * 2));
        }

        // sort by choosing the minimum of both subarrays
        int index1 = L, index2 = M + 1, index3 = 0;
        while (index1 <= M && index2 <= R) {
            if (id[index1] <= id[index2]) {
                set.remove(new Node(id[index1], W[index1] * 2));
                temp[index3] = id[index1];
                tempW[index3++] = W[index1++];
            } else {
                swaps += set.tailSet(new Node(0, (W[index2] + D) * 2 + 1)).size();
                swaps += set.headSet(new Node(0, (W[index2] - D) * 2 - 1)).size();
                temp[index3] = id[index2];
                tempW[index3++] = W[index2++];
            }
        }

        // add any remaining elements in left subarray
        while (index1 <= M) {
            temp[index3] = id[index1];
            tempW[index3++] = W[index1++];
        }

        // add any remaining elements in right subarray
        while (index2 <= R) {
            temp[index3] = id[index2];
            tempW[index3++] = W[index2++];
        }

        // transfer elements back into original array
        System.arraycopy(temp, 0, id, L, index3);
        System.arraycopy(tempW, 0, W, L, index3);

        return swaps;
    }

    public static void main(String[] args) throws IOException {
        Reader sc = new Reader();
        int N = sc.nextInt();
        int D = sc.nextInt();
        int W[] = new int[N];
        int id[] = new int[N];
        for (int i = 0; i < N; ++i) {
            W[i] = sc.nextInt();
        }
        for (int i = 0; i < N; ++i) {
            id[i] = sc.nextInt();
        }
        System.out.println(countSwaps(0, N - 1, W, id, new int[N], D));
    }

    private static class Reader {
        final private int BUFFER_SIZE = 1 << 16;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;

        public Reader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public int nextInt() throws IOException {
            byte c;
            while ((c = read()) <= ' ');
            int v = 0;
            do { v = (v << 3) + (v << 1) + (c ^ '0'); }
            while ((c = read()) >= '0' && c <= '9');
            return v;
        }

        private void fillBuffer() throws IOException {
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (bytesRead == -1) buffer[0] = -1;
        }

        private byte read() throws IOException {
            if (bufferPointer == bytesRead) fillBuffer();
            return buffer[bufferPointer++];
        }
    }
}

class Node {
    public int id;
    public int weight;
    Node(int id, int weight){
        this.id = id;
        this.weight = weight;
    }
}