package com.example;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) {
        port(5001);

        // Health-check route
        get("/health", (req, res) -> "Service is running!");

        // Solve recurrence relation
        post("/solve", (req, res) -> {
            res.type("application/json");
            Map<String, String> result = new HashMap<>();
            try {
                Map<String, String> data = new Gson().fromJson(req.body(), Map.class);
                if (data == null || !data.containsKey("recurrence_relation")) {
                    throw new IllegalArgumentException("Request must contain 'recurrence_relation' field.");
                }

                String recurrence = data.get("recurrence_relation");
                if (recurrence == null || recurrence.isEmpty()) {
                    throw new IllegalArgumentException("Recurrence relation cannot be null or empty.");
                }

                // Solve the recurrence relation (mocked solution for now)
                String bigO = solveRecurrence(recurrence);
                result.put("big_o", bigO);
                return new Gson().toJson(result);
            } catch (Exception e) {
                res.status(400);
                result.put("error", e.getMessage());
                return new Gson().toJson(result);
            }
        });
    }

    private static String solveRecurrence(String recurrence) throws Exception {
        // Example solving logic (replace with real implementation)
        if (recurrence.contains("T(n/2)")) {
            return "O(n log n)";
        }
        return "O(n)";
    }
}
