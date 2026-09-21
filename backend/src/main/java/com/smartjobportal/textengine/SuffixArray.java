package com.smartjobportal.textengine;

import java.util.*;

public class SuffixArray {
    public static int[] build(String s) {
        int n = s.length();
        Integer[] sa = new Integer[n];
        for (int i = 0; i < n; i++) sa[i] = i;
        Arrays.sort(sa, (a, b) -> s.substring(a).compareTo(s.substring(b)));
        int[] result = new int[n];
        for (int i = 0; i < n; i++) result[i] = sa[i];
        return result;
    }

    public static int[] buildFast(String s) {
        int n = s.length();
        int[] sa = new int[n];
        int[] rank = new int[n];
        int[] tmp = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = i;
            rank[i] = s.charAt(i);
        }
        for (int k = 1; k < n; k *= 2) {
            final int kk = k;
            Comparator<Integer> cmp = (a, b) -> {
                if (rank[a] != rank[b]) return Integer.compare(rank[a], rank[b]);
                int ra = a + kk < n ? rank[a + kk] : -1;
                int rb = b + kk < n ? rank[b + kk] : -1;
                return Integer.compare(ra, rb);
            };
            Arrays.sort(sa, cmp);
            tmp[sa[0]] = 0;
            for (int i = 1; i < n; i++) {
                tmp[sa[i]] = tmp[sa[i - 1]] + (cmp.compare(sa[i - 1], sa[i]) < 0 ? 1 : 0);
            }
            System.arraycopy(tmp, 0, rank, 0, n);
        }
        return sa;
    }

    public static List<String> getSuffixes(String s, int[] sa) {
        List<String> result = new ArrayList<>();
        for (int idx : sa) {
            result.add(s.substring(idx));
        }
        return result;
    }
}
