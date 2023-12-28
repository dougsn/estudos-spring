package com.estudos.services.v1;

import com.estudos.data.dto.user.UserDTO;
import com.estudos.data.dto.user.UserDTOMapper;
import com.estudos.data.dto.user.UserInfoDTO;
import com.estudos.data.dto.user.UserInfoDTOMapper;
import com.estudos.repository.UserRepository;
import com.estudos.services.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServices {


    private final UserRepository repository;
    private final UserDTOMapper mapper;
    private final UserInfoDTOMapper infoMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserInfoDTO> findById(Long id) {
        return Optional.of(repository.findById(id)
                .map(infoMapper)
                .orElseThrow(() -> new ObjectNotFoundException("User ID: " + id + " not found.")));
    }

//    @Transactional
//    public Optional<UserInfoDTO> updateUserWithoutAgency(UserUpdateDTO request) {
//        userAlreadyRegistered(request);
//
//        var userAt = repository.findById(request.id());
//
//        var user = User.builder()
//                .id(request.id())
//                .fullName(request.fullName())
//                .email(request.email())
//                .password(passwordEncoder.encode(request.password()))
//                .role(Role.AGENCYOWNER)
//                .build();
//        if (request.password().isEmpty()) {
//            user.setPassword(userAt.get().getPassword());
//        } else {
//            user.setPassword(passwordEncoder.encode(request.password()));
//        }
//        if (request.role() == null) user.setRole(userAt.get().getRole());
//        return Optional.of(infoMapper.apply(repository.save(user)));
//    }

    @Transactional
    public Boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }


}



























