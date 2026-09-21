package com.smartjobportal.textengine;

import java.util.*;

public class RabinKarp {
    private static final long BASE = 257;
    private static final long MOD1 = 1_000_000_007;
    private static final long MOD2 = 1_000_000_009;

    public static List<Integer> search(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        if (pattern == null || pattern.isEmpty() || text == null() || pattern.length() > text.length()) {
            return result;
        }
        int n = text.length(), m = pattern.length();
        long h1 = 1, h2 = 1;
        for (int i = 0; i < m - 1; i++) {
            h1 = (h1 * BASE) % MOD1;
            h2 = (h2 * BASE) % MOD2;
        }
        long pHash1 = 0, pHash2 = 0;
        long tHash1 = 0, tHash2 = 0;
        for (int i = 0; i < m; i++) {
            pHash1 = (pHash1 * BASE + pattern.charAt(i)) % MOD1;
            tHash1 = (tHash1 * BASE + text.charAt(i)) % MOD1;
            pHash2 = (pHash2 * BASE + pattern.charAt(i)) % MOD2;
            tHash2 = (tHash2 * BASE + text.charAt(i)) % MOD2;
        }
        for (int i = 0; i <= n - m; i++) {
            if (pHash1 == tHash1 && pHash2 == tHash2) {
                if (text.substring(i, i + m).equals(pattern)) {
                    result.add(i);
                }
            }
            if (i < n - m) {
                tHash1 = (tHash1 - text.charAt(i) * h1) % MOD1;
                if (tHash1 < 0) tHash1 += MOD1;
                tHash1 = (tHash1 * BASE + text.charAt(i + m)) % MOD1;

                tHash2 = (tHash2 - text.charAt(i) * h2) % MOD2;
                if (tHash2 < 0) tHash2 += MOD2;
                tHash2 = (tHash2 * BASE + text.charAt(i + m)) % MOD2;
            }
        }
        return result;
    }
}
