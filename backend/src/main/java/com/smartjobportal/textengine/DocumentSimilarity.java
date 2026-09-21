package com.smartjobportal.textengine;

import java.util.*;

public class DocumentSimilarity {
    public static double jaccardSimilarity(String a, String b) {
        if (a == null || b == null) return 0.0;
        Set<String> setA = new HashSet<>(Arrays.asList(a.toLowerCase().split("\\s+")));
        Set<String> setB = new HashSet<>(Arrays.asList(b.toLowerCase().split("\\s+")));
        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);
        Set<String> union = new HashSet<>(setA);
        union.addAll(setB);
        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }

    public static double cosineSimilarity(String a, String b) {
        if (a == null || b == null) return 0.0;
        Map<String, int[]> freq = new HashMap<>();
        String[] wordsA = a.toLowerCase().split("\\s+");
        String[] wordsB = b.toLowerCase().split("\\s+");
        for (String w : wordsA) {
            freq.computeIfAbsent(w, k -> new int[2])[0]++;
        }
        for (String w : wordsB) {
            freq.computeIfAbsent(w, k -> new int[2])[1]++;
        }
        double dot = 0, normA = 0, normB = 0;
        for (int[] counts : freq.values()) {
            dot += counts[0] * counts[1];
            normA += counts[0] * counts[0];
            normB += counts[1] * counts[1];
        }
        if (normA == 0 || normB == 0) return 0.0;
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public static double longestCommonSubstringSimilarity(String a, String b) {
        if (a == null || b == null) return 0.0;
        int maxLen = 0;
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    maxLen = Math.max(maxLen, dp[i][j]);
                }
            }
        }
        int minLen = Math.min(a.length(), b.length());
        return minLen == 0 ? 0.0 : (double) maxLen / minLen;
    }
}
