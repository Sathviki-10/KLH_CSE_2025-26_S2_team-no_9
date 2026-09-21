package com.smartjobportal.textengine;

import java.util.*;

public class ZFunction {
    public static int[] compute(String s) {
        int n = s.length();
        int[] z = new int[n];
        int l = 0, r = 0;
        for (int i = 1; i < n; i++) {
            if (i <= r) {
                z[i] = Math.min(r - i + 1, z[i - l]);
            }
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) {
                z[i]++;
            }
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }
        return z;
    }

    public static List<Integer> search(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        if (pattern == null || pattern.isEmpty() || text == null || text.isEmpty()) {
            return result;
        }
        String combined = pattern + "$" + text;
        int[] z = compute(combined);
        for (int i = pattern.length() + 1; i < combined.length(); i++) {
            if (z[i] == pattern.length()) {
                result.add(i - pattern.length() - 1);
            }
        }
        return result;
    }
}
