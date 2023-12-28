package com.estudos.data.dto.user;


import com.estudos.data.model.Permission;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record UserInfoDTO(
        Long id,
        @Schema(type = "string", example = "John Doe")
        String username,
        @Schema(type = "string", example = "johndoe@gmail.com")
        List<Permission> permissions
) {
}
