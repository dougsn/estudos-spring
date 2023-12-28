package com.estudos.integrationtests.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    @NotBlank
    @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb3VnbGFzQGdtYWlsLmNvbSIsImlhdCI6MTY5NTM4MjM2NSwiZXhwIjoxNjk1NDI1NTY1fQ.6Ya3lP6pJiSPt1Rsn4vnSjWd013AbUz3x-UNHbsjv_4")
    private String token;
}
