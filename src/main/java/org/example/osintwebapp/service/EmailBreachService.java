package org.example.osintwebapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.example.osintwebapp.model.EmailVerificationResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailBreachService {
    private static final String HUNTER_API_KEY = "3bcc37b4520d497dbc0db86142835349307e79c9";

    public EmailVerificationResponse checkEmail(String email) {
        try {
            String url = String.format("https://api.hunter.io/v2/email-verifier?email=%s&api_key=%s",
                    email, HUNTER_API_KEY);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            JsonNode data = root.path("data");

            EmailVerificationResponse verificationResponse = new EmailVerificationResponse(
                    data.path("email").asText(),
                    data.path("result").asText(),
                    data.path("score").asInt(),
                    data.path("sources").toString()
            );

            return verificationResponse;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new EmailVerificationResponse("", "Error", 0, "[]");
        }
    }
    
}
