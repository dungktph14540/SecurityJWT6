package com.blo.security6.configuration;

import com.blo.security6.entity.Users;
import com.blo.security6.enums.Roles;
import com.blo.security6.repository.RepositoryUsers;
import java.util.HashSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
@RequiredArgsConstructor
public class AppliactionInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(RepositoryUsers userRepository){
        return args -> {
            if(userRepository.findByEmail("marshall1000@gmail.com").isEmpty()){
                var roles = new HashSet<String>();
                roles.add(Roles.ADMIN.name());

                Users users = Users.builder().email("marshall1000@gmail.com")
                        .password(passwordEncoder.encode("123456789"))
                        .role(roles)
                        .build();
                userRepository.save(users);
                log.warn("Admin user har been create with default password: 123456789, please change it");
            }


        };
    }
}
