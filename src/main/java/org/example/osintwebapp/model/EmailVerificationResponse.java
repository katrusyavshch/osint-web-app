package org.example.osintwebapp.model;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailVerificationResponse {
    private String email;
    private String status;
    private int score;
    private List<Map<String, Object>> sources;
}
