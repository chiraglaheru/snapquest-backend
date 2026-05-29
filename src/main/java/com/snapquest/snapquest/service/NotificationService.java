package com.snapquest.snapquest.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class NotificationService {

    public void sendPush(
            String expoPushToken,
            String title,
            String body
    ) {

        try {

            RestTemplate restTemplate =
                    new RestTemplate();

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON
            );

            Map<String, Object> payload =
                    Map.of(
                            "to", expoPushToken,
                            "title", title,
                            "body", body
                    );

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(
                            payload,
                            headers
                    );

            restTemplate.postForEntity(
                    "https://exp.host/--/api/v2/push/send",
                    request,
                    String.class
            );

            System.out.println(
                    "PUSH SENT TO: "
                            + expoPushToken
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}