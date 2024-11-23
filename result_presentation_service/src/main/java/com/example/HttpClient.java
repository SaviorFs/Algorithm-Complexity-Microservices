package com.example;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpClient {
    public static String post(String urlString, String jsonPayload) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        // Set timeouts
        conn.setConnectTimeout(5000); // Connection timeout: 5 seconds
        conn.setReadTimeout(5000);    // Read timeout: 5 seconds

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonPayload.getBytes());
            os.flush();
        }

        int responseCode = conn.getResponseCode();

        try (Scanner scanner = new Scanner(
                responseCode == 200 ? conn.getInputStream() : conn.getErrorStream(),
                "UTF-8")) {
            String response = scanner.useDelimiter("\\A").next();
            if (responseCode != 200) {
                throw new RuntimeException("HTTP error code: " + responseCode + " Response: " + response);
            }
            return response;
        }
    }
}
