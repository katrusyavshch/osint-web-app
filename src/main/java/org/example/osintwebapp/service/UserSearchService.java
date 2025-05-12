package org.example.osintwebapp.service;
import static org.example.osintwebapp.config.ApiUrls.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserSearchService {

    @Value("${bearer.token}")
    private String bearerToken;

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public Map<String, String> searchUserAccounts(String username) {
        Map<String, String> result = new HashMap<>();

        result.put("github", checkAccount(GITHUB_URL + username, "https://github.com/" + username));
        result.put("twitter", checkTwitterAccount(username));
        result.put("reddit", checkAccount(REDDIT_URL + username + "/about.json", "https://www.reddit.com/user/" + username));
        result.put("telegram", checkAccount(TELEGRAM_URL + username, "https://t.me/" + username));

        return result;
    }

    private String checkAccount(String urlToCheck, String validUrlIfFound) {
        try {
            HttpResponse response = sendHttpRequest(urlToCheck);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return validUrlIfFound;
            } else {
                return "Не знайдено";
            }
        } catch (IOException e) {
            log.error("Error while connecting to URL: " + urlToCheck, e);
            return null;
        }
    }

    private String checkTwitterAccount(String username) {
        String twitterApiUrl = TWITTER_URL + username;
        HttpGet twitterRequest = new HttpGet(twitterApiUrl);
        twitterRequest.setHeader("Authorization", bearerToken);

        try {
            HttpResponse response = httpClient.execute(twitterRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return "https://twitter.com/" + username;
            } else if (statusCode == 404) {
                return "Не знайдено";
            } else {
                return "Помилка: " + statusCode;
            }
        } catch (IOException e) {
            log.error("Error while connecting to Twitter API for user: " + username, e);
            return null;
        }
    }

    // Helper method to send HTTP requests and return the response
    private HttpResponse sendHttpRequest(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        return httpClient.execute(request);
    }
}
