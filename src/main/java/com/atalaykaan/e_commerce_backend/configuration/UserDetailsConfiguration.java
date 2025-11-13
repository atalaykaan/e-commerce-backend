package com.atalaykaan.e_commerce_backend.configuration;

import com.atalaykaan.e_commerce_backend.exception.UserNotFoundException;
import com.atalaykaan.e_commerce_backend.model.User;
import com.atalaykaan.e_commerce_backend.model.UserPrincipal;
import com.atalaykaan.e_commerce_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class UserDetailsConfiguration implements UserDetailsService {

    private final UserRepository userRepository;

    //in this project, email was used instead of username.
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return new UserPrincipal(user);
    }
}