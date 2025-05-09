package org.example.osintwebapp.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.osintwebapp.model.EmailRequest;
import org.example.osintwebapp.model.EmailVerificationResponse;
import org.example.osintwebapp.service.EmailBreachService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailBreachController {
    private final EmailBreachService emailBreachService;


    @PostMapping("/verify")
    public EmailVerificationResponse verifyEmail(@RequestBody EmailRequest request) {
        return emailBreachService.checkEmail(request.getEmail());
    }
}
