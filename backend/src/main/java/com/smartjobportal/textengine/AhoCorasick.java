package com.smartjobportal.textengine;

import java.util.*;

public class AhoCorasick {
    private static class Node {
        Map<Character, Node> next = new HashMap<>();
        Node fail;
        List<Integer> output = new ArrayList<>();
    }

    private final Node root = new Node();

    public AhoCorasick(List<String> patterns) {
        for (int i = 0; i < patterns.size(); i++) {
            insert(patterns.get(i), i);
        }
        buildFailureLinks();
    }

    private void insert(String pattern, int index) {
        Node current = root;
        for (char ch : pattern.toCharArray()) {
            current = current.next.computeIfAbsent(ch, c -> new Node());
        }
        current.output.add(index);
    }

    private void buildFailureLinks() {
        Queue<Node> queue = new LinkedList<>();
        for (Node child : root.next.values()) {
            child.fail = root;
            queue.add(child);
        }
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            for (Map.Entry<Character, Node> entry : current.next.entrySet()) {
                char ch = entry.getKey();
                Node child = entry.getValue();
                Node fail = current.fail;
                while (fail != null && !fail.next.containsKey(ch)) {
                    fail = fail.fail;
                }
                child.fail = fail != null ? fail.next.get(ch) : root;
                child.output.addAll(child.fail.output);
                queue.add(child);
            }
        }
    }

    public Map<Integer, List<Integer>> search(String text) {
        Map<Integer, List<Integer>> result = new HashMap<>();
        Node current = root;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            while (current != null && !current.next.containsKey(ch)) {
                current = current.fail;
            }
            current = current != null ? current.next.get(ch) : root;
            if (current == null) current = root;
            for (int patIdx : current.output) {
                result.computeIfAbsent(patIdx, k -> new ArrayList<>()).add(i);
            }
        }
        return result;
    }
}
