package org.example.osintwebapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailVerificationResponse {
    private String email;
    private String status;
    private int score;
    private String sources;
}
