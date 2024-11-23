package com.example;

import static spark.Spark.*;
import com.google.gson.Gson;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        port(5002);

        // Health-check route
        get("/health", (req, res) -> "Service is running!");

        // Root route to render the form
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return createFreeMarkerEngine().render(new ModelAndView(model, "index.ftl"));
        });

        // Handle form submission
        post("/analyze", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            try {
                String code = req.queryParams("code");
                model.put("code", code);

                // Call Time Complexity Service
                String recurrenceRelation = getRecurrenceRelation(code);

                // Call Recurrence Relation Service
                String bigO = solveRecurrence(recurrenceRelation);

                // Populate model with results
                model.put("recurrence_relation", recurrenceRelation);
                model.put("big_o", bigO);

                return createFreeMarkerEngine().render(new ModelAndView(model, "result.ftl"));
            } catch (Exception e) {
                model.put("error", e.getMessage());
                return createFreeMarkerEngine().render(new ModelAndView(model, "index.ftl"));
            }
        });
    }

    private static String getRecurrenceRelation(String code) throws Exception {
        Map<String, String> payload = new HashMap<>();
        payload.put("code", code);
        String response = HttpClient.post("http://localhost:5000/analyze", new Gson().toJson(payload));

        Map<String, String> result = new Gson().fromJson(response, Map.class);
        if (result.containsKey("recurrence_relation")) {
            return result.get("recurrence_relation");
        } else if (result.containsKey("error")) {
            throw new Exception("Time Complexity Service Error: " + result.get("error"));
        } else {
            throw new Exception("Unexpected response from Time Complexity Service");
        }
    }

    private static String solveRecurrence(String recurrence) throws Exception {
        Map<String, String> payload = new HashMap<>();
        payload.put("recurrence_relation", recurrence);
        String response = HttpClient.post("http://localhost:5001/solve", new Gson().toJson(payload));

        Map<String, String> result = new Gson().fromJson(response, Map.class);
        if (result.containsKey("big_o")) {
            return result.get("big_o");
        } else if (result.containsKey("error")) {
            throw new Exception("Recurrence Relation Service Error: " + result.get("error"));
        } else {
            throw new Exception("Unexpected response from Recurrence Relation Service");
        }
    }

    private static FreeMarkerEngine createFreeMarkerEngine() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setClassForTemplateLoading(App.class, "/templates");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return new FreeMarkerEngine(configuration);
    }
}
