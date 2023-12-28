package com.estudos.data.dto.user;

import com.estudos.data.model.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;


@Service
public class UserInfoDTOMapper implements Function<User, UserInfoDTO> {
    @Override
    public UserInfoDTO apply(User user) {
        return new UserInfoDTO(
                user.getId(),
                user.getUsername(),
                user.getPermissions()
        );
    }
}