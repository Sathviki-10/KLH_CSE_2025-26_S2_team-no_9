package com.smartjobportal.textengine;

import java.util.*;

public class SuffixAutomaton {
    private static class State {
        Map<Character, State> next = new HashMap<>();
        State link;
        int len;
    }

    private final State root = new State();
    private State last = root;

    public SuffixAutomaton(String s) {
        for (char ch : s.toCharArray()) extend(ch);
    }

    private void extend(char ch) {
        State cur = new State();
        cur.len = last.len + 1;
        State p = last;
        while (p != null && !p.next.containsKey(ch)) {
            p.next.put(ch, cur);
            p = p.link;
        }
        if (p == null) {
            cur.link = root;
        } else {
            State q = p.next.get(ch);
            if (p.len + 1 == q.len) {
                cur.link = q;
            } else {
                State clone = new State();
                clone.len = p.len + 1;
                clone.next = new HashMap<>(q.next);
                clone.link = q.link;
                while (p != null && p.next.get(ch) == q) {
                    p.next.put(ch, clone);
                    p = p.link;
                }
                q.link = clone;
                cur.link = clone;
            }
        }
        last = cur;
    }

    public boolean contains(String pattern) {
        State current = root;
        for (char ch : pattern.toCharArray()) {
            if (!current.next.containsKey(ch)) return false;
            current = current.next.get(ch);
        }
        return true;
    }

    public int distinctSubstringsCount() {
        Set<String> set = new HashSet<>();
        collectSubstrings(root, new StringBuilder(), set);
        return set.size();
    }

    private void collectSubstrings(State state, StringBuilder sb, Set<String> set) {
        if (state != root) {
            set.add(sb.toString());
        }
        for (Map.Entry<Character, State> entry : state.next.entrySet()) {
            sb.append(entry.getKey());
            collectSubstrings(entry.getValue(), sb, set);
            sb.deleteCharAt(sb.length() - 1);
        }
    }
}
