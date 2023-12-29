package com.estudos.config;

import com.estudos.data.model.User;
import com.estudos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

//        if (userRepository.findByUsername("Administrator").isEmpty()){
//            var userSystem = User.builder()
//                    .userName("Administrator")
//                    .password(passwordEncoder.encode("admin123"))
//                    .build();
//            userRepository.save(userSystem);
//        }
    }
}
