package com.estudos.integrationtests.vo;

import com.estudos.data.model.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
        Long id;
        String username;
        List<Permission> permissions;
}
