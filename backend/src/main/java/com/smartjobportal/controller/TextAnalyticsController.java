package com.smartjobportal.controller;

import com.smartjobportal.textengine.TextHackEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/text")
@CrossOrigin(origins = "*")
public class TextAnalyticsController {

    @PostMapping("/pattern-search")
    public ResponseEntity<Map<String, Object>> patternSearch(@RequestBody Map<String, String> request) {
        String text = request.getOrDefault("text", "");
        String pattern = request.getOrDefault("pattern", "");
        Map<String, Object> result = TextHackEngine.patternSearch(text, pattern);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/fuzzy-match")
    public ResponseEntity<Map<String, Object>> fuzzyMatch(@RequestBody Map<String, Object> request) {
        String text = (String) request.getOrDefault("text", "");
        String pattern = (String) request.getOrDefault("pattern", "");
        int maxDistance = ((Number) request.getOrDefault("maxDistance", 2)).intValue();
        Map<String, Object> result = TextHackEngine.fuzzyMatch(text, pattern, maxDistance);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/similarity")
    public ResponseEntity<Map<String, Object>> similarity(@RequestBody Map<String, String> request) {
        String docA = request.getOrDefault("docA", "");
        String docB = request.getOrDefault("docB", "");
        Map<String, Object> result = TextHackEngine.documentSimilarity(docA, docB);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/suffix-analysis")
    public ResponseEntity<Map<String, Object>> suffixAnalysis(@RequestBody Map<String, String> request) {
        String text = request.getOrDefault("text", "");
        Map<String, Object> result = TextHackEngine.suffixAnalysis(text);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/multi-pattern")
    public ResponseEntity<Map<String, Object>> multiPattern(@RequestBody Map<String, Object> request) {
        String text = (String) request.getOrDefault("text", "");
        List<String> patterns = (List<String>) request.getOrDefault("patterns", Collections.emptyList());
        Map<String, Object> result = TextHackEngine.multiPatternSearch(text, patterns);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/primality")
    public ResponseEntity<Map<String, Object>> primality(@RequestBody Map<String, Object> request) {
        long n = ((Number) request.getOrDefault("number", 0)).longValue();
        Map<String, Object> result = TextHackEngine.primalityTest(n);
        return ResponseEntity.ok(result);
    }
}
