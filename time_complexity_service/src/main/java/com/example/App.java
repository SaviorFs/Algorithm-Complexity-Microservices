package com.example;

import static spark.Spark.*;
import com.google.gson.Gson;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        port(5000);

        // Health-check route
        get("/health", (req, res) -> "Service is running!");

        // Analyze code for time complexity
        post("/analyze", (req, res) -> {
            res.type("application/json");
            Map<String, String> result = new HashMap<>();
            try {
                Map<String, String> data = new Gson().fromJson(req.body(), Map.class);
                if (data == null || !data.containsKey("code")) {
                    throw new IllegalArgumentException("Request must contain 'code' field.");
                }

                String code = data.get("code");
                if (code == null || code.isEmpty()) {
                    throw new IllegalArgumentException("Code cannot be null or empty.");
                }

                // Extract recurrence relation from the provided code
                String recurrenceRelation = extractRecurrenceRelation(code);
                result.put("recurrence_relation", recurrenceRelation);
                return new Gson().toJson(result);
            } catch (Exception e) {
                res.status(400);
                result.put("error", e.getMessage());
                return new Gson().toJson(result);
            }
        });
    }

    public static String extractRecurrenceRelation(String code) throws Exception {
        try {
            // Wrap the code in a temporary class if it's not a full class
            String wrappedCode = "class TempClass { " + code + " }";
            CompilationUnit cu = StaticJavaParser.parse(wrappedCode);

            MethodDeclaration method = cu.getClassByName("TempClass")
                .orElseThrow(() -> new Exception("No class found"))
                .getMethods().get(0);

            // Example: Replace with actual recurrence extraction logic
            return "T(n) = 2T(n/2) + O(n)";
        } catch (Exception e) {
            throw new Exception("Parse error: " + e.getMessage(), e);
        }
    }
}
