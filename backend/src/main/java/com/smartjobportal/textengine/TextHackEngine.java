package com.smartjobportal.textengine;

import java.util.*;

public class TextHackEngine {
    public static Map<String, Object> patternSearch(String text, String pattern) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (text == null || pattern == null) return result;
        long start = System.currentTimeMillis();
        List<Integer> kmp = KmpSearch.search(text, pattern);
        List<Integer> zfun = ZFunction.search(text, pattern);
        List<Integer> rk = RabinKarp.search(text, pattern);
        long end = System.currentTimeMillis();
        result.put("algorithm", "Pattern Search");
        result.put("pattern", pattern);
        result.put("textLength", text.length());
        result.put("kmpMatches", kmp);
        result.put("zFunctionMatches", zfun);
        result.put("rabinKarpMatches", rk);
        result.put("matchCount", kmp.size());
        result.put("timeMs", end - start);
        return result;
    }

    public static Map<String, Object> fuzzyMatch(String text, String pattern, int maxDistance) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (text == null || pattern == null) return result;
        long start = System.currentTimeMillis();
        List<Integer> positions = new ArrayList<>();
        int window = pattern.length() + maxDistance;
        for (int i = 0; i <= text.length() - pattern.length(); i++) {
            String sub = text.substring(i, Math.min(i + window, text.length()));
            int dist = EditDistance.levenshtein(pattern, sub.substring(0, Math.min(pattern.length() + maxDistance, sub.length())));
            if (dist <= maxDistance) {
                positions.add(i);
            }
        }
        long end = System.currentTimeMillis();
        result.put("algorithm", "Fuzzy Match");
        result.put("pattern", pattern);
        result.put("maxEditDistance", maxDistance);
        result.put("positions", positions);
        result.put("count", positions.size());
        result.put("timeMs", end - start);
        return result;
    }

    public static Map<String, Object> documentSimilarity(String docA, String docB) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (docA == null || docB == null) return result;
        long start = System.currentTimeMillis();
        double jaccard = DocumentSimilarity.jaccardSimilarity(docA, docB);
        double cosine = DocumentSimilarity.cosineSimilarity(docA, docB);
        double lcs = DocumentSimilarity.longestCommonSubstringSimilarity(docA, docB);
        long end = System.currentTimeMillis();
        result.put("algorithm", "Document Similarity");
        result.put("jaccard", Math.round(jaccard * 10000) / 10000.0);
        result.put("cosine", Math.round(cosine * 10000) / 10000.0);
        result.put("lcsSimilarity", Math.round(lcs * 10000) / 10000.0);
        result.put("timeMs", end - start);
        return result;
    }

    public static Map<String, Object> suffixAnalysis(String text) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (text == null || text.isEmpty()) return result;
        long start = System.currentTimeMillis();
        int[] sa = SuffixArray.buildFast(text);
        int[] lcp = LCPKasai.build(text, sa);
        List<String> suffixes = SuffixArray.getSuffixes(text, sa);
        SuffixAutomaton automaton = new SuffixAutomaton(text);
        long end = System.currentTimeMillis();
        result.put("algorithm", "Suffix Analysis");
        result.put("textLength", text.length());
        result.put("suffixArray", Arrays.toString(sa));
        result.put("lcpArray", Arrays.toString(lcp));
        result.put("sampleSuffixes", suffixes.subList(0, Math.min(10, suffixes.size())));
        result.put("distinctSubstrings", automaton.distinctSubstringsCount());
        result.put("timeMs", end - start);
        return result;
    }

    public static Map<String, Object> multiPatternSearch(String text, List<String> patterns) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (text == null || patterns == null || patterns.isEmpty()) return result;
        long start = System.currentTimeMillis();
        AhoCorasick ac = new AhoCorasick(patterns);
        Map<Integer, List<Integer>> matches = ac.search(text);
        long end = System.currentTimeMillis();
        result.put("algorithm", "Multi-Pattern Search");
        result.put("textLength", text.length());
        result.put("patternCount", patterns.size());
        result.put("matches", matches);
        result.put("timeMs", end - start);
        return result;
    }

    public static Map<String, Object> primalityTest(long n) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("algorithm", "Primality Test");
        result.put("number", n);
        long start = System.currentTimeMillis();
        boolean isPrime = isPrimeMillerRabin(n);
        long end = System.currentTimeMillis();
        result.put("isPrime", isPrime);
        result.put("timeMs", end - start);
        return result;
    }

    private static boolean isPrimeMillerRabin(long n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0) return false;
        long d = n - 1;
        int s = 0;
        while (d % 2 == 0) {
            d /= 2;
            s++;
        }
        long[] bases = {2, 3, 5, 7, 11, 13};
        for (long a : bases) {
            if (a >= n) continue;
            long x = modPow(a, d, n);
            if (x == 1 || x == n - 1) continue;
            boolean composite = true;
            for (int r = 1; r < s; r++) {
                x = modPow(x, 2, n);
                if (x == n - 1) {
                    composite = false;
                    break;
                }
            }
            if (composite) return false;
        }
        return true;
    }

    private static long modPow(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = (result * base) % mod;
            base = (base * base) % mod;
            exp >>= 1;
        }
        return result;
    }
}
