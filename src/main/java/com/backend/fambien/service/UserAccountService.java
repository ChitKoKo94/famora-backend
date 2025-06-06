package com.backend.fambien.service;

import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface UserAccountService {

    Mono<UserDetails> authenticateUser(String username, String password);
}
