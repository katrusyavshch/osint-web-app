package org.example.osintwebapp.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CombinedRequest {
    private String email;
    private String username;
}
