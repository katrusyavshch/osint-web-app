package org.example.osintwebapp.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.osintwebapp.model.CombinedRequest;
import org.example.osintwebapp.model.EmailVerificationResponse;
import org.example.osintwebapp.model.SearchRequest;
import org.example.osintwebapp.service.EmailBreachService;
import org.example.osintwebapp.service.UserSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/combined")
@RequiredArgsConstructor
public class CombinedController {
    private final EmailBreachService emailBreachService;
    private final UserSearchService userSearchService;

    @PostMapping("/verify-and-search")
    public ResponseEntity<Map<String, Object>> verifyEmailAndSearchUser(@RequestBody CombinedRequest request) {
        EmailVerificationResponse emailResponse = emailBreachService.checkEmail(request.getEmail());

        Map<String, String> searchResult = userSearchService.searchUserAccounts(request.getUsername());

        if (searchResult.containsValue(null)) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("userSearch", searchResult));
        }

        Map<String, Object> combinedResult = Map.of(
                "emailVerification", emailResponse,
                "userSearch", searchResult
        );

        return ResponseEntity.ok(combinedResult);
    }
}
