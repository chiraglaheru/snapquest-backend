package com.snapquest.snapquest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class VisionService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/" +
                    "gemini-2.5-flash:generateContent";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public boolean verifyImage(String imageBase64, String challenge) {
        try {
            String body = buildRequestBody(imageBase64, challenge);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("VISION RESPONSE: " + response.body());

            if (response.statusCode() != 200) {
                System.out.println("Gemini API failed with status: " + response.statusCode());
                return false;
            }

            return parseResult(response.body());

        } catch (Exception e) {
            System.out.println("Vision API error: " + e.getMessage());
            return false;
        }
    }

    private String buildRequestBody(String imageBase64, String challenge) {
        String prompt = "You are verifying a scavenger hunt photo. " +
                "Challenge: " + challenge + ". " +
                "If the requested object or scene is reasonably visible in the image, answer YES. " +
                "Do not be overly strict about angles, lighting, blur, or partial visibility. " +
                "Only answer NO if the requested object is clearly absent. " +
                "Respond with exactly one word only: YES or NO.";

        return """
            {
                "contents": [{
                    "parts": [
                        {
                            "inline_data": {
                                "mime_type": "image/jpeg",
                                "data": "%s"
                            }
                        },
                        { "text": "%s" }
                    ]
                }],
                "generationConfig": {
                    "maxOutputTokens": 100,
                    "temperature": 0
                }
            }
            """.formatted(imageBase64, escapeJson(prompt));
    }

    private boolean parseResult(String responseBody) {
        String upper = responseBody.toUpperCase();

        System.out.println("RAW RESPONSE: " + upper);

        return upper.contains("\"TEXT\": \"YES\"");
    }

    private String escapeJson(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}