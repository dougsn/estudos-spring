package com.estudos.services.v1;

import com.estudos.data.dto.security.AuthenticationRequest;
import com.estudos.data.dto.security.AuthenticationResponse;
import com.estudos.data.dto.security.RegisterRequest;
import com.estudos.data.dto.user.UserInfoDTO;
import com.estudos.data.dto.user.UserInfoDTOMapper;
import com.estudos.data.model.User;
import com.estudos.repository.UserRepository;
import com.estudos.security.jwt.JwtService;
import com.estudos.services.exceptions.DataIntegratyViolationException;
import com.estudos.services.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServices {
    private final UserRepository repository;
    private final UserInfoDTOMapper userInfoDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        userAlreadyRegistered(request);

        var user = User.builder()
                .userName(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Incorrect credentials."));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );


        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    @Transactional(readOnly = true)
    public Optional<UserInfoDTO> infoUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getName();
        String user = (String) principal;
        User userEntity = repository.findByUsername(user).orElseThrow(() -> new ObjectNotFoundException("User not found."));

        return Optional.of(userInfoDTOMapper.apply(userEntity));
    }


    @Transactional(readOnly = true)
    public void userAlreadyRegistered(RegisterRequest data) {
        Optional<User> userLogin = repository.findByUsername(data.getUsername());

        if (userLogin.isPresent())
            throw new DataIntegratyViolationException("User already registered.");
    }

}