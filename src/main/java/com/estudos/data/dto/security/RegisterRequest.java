package com.estudos.data.dto.security;

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
public class RegisterRequest {
    @Schema(type = "string", example = "John Doe")
    @NotBlank(message = "The [username] field is mandatory.")
    private String username;
    @Schema(type = "string", example = "#Password!")
    @NotBlank(message = "The [password] field is mandatory.")
    private String password;
}
