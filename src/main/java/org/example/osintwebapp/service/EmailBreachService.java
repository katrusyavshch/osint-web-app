package org.example.osintwebapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.osintwebapp.model.EmailVerificationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailBreachService {

    @Value("${hunter.api.key}")
    private String hunterApiKey;

    public EmailVerificationResponse checkEmail(String email) {
        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);

        try {
            String url = String.format("https://api.hunter.io/v2/email-verifier?email=%s&api_key=%s",
                    encodedEmail, hunterApiKey);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            JsonNode data = root.path("data");

            List<Map<String, Object>> sources = mapper.convertValue(
                    data.path("sources"),
                    new com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Object>>>() {}
            );

            EmailVerificationResponse verificationResponse = new EmailVerificationResponse(
                    data.path("email").asText(),
                    data.path("result").asText(),
                    data.path("score").asInt(),
                    sources
            );

            return verificationResponse;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new EmailVerificationResponse("", "Error", 0, List.of());
        }
    }
    
}
