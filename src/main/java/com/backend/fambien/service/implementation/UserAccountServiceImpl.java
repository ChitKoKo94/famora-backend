package com.backend.fambien.service.implementation;

import com.backend.fambien.repository.UserRepo;
import com.backend.fambien.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
class UserAccountServiceImpl implements UserAccountService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDetails> authenticateUser(String username, String password) {
        return userRepo.findByUsername(username)
                .cast(UserDetails.class)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new BadCredentialsException("User not found: " + username))
                ))
                .flatMap(userDetails ->
                     passwordEncoder.matches(password, userDetails.getPassword()) ?
                             Mono.just(userDetails) :
                             Mono.error(new BadCredentialsException("Invalid password for user: " + username))
                );
    }
}
