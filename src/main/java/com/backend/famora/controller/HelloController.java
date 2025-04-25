package com.backend.famora.controller;

import com.backend.famora.entity.UserProfile;
import com.backend.famora.repository.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {
    @Autowired
    UserProfileRepo repo;

    @GetMapping("hello")
    public Mono<ResponseEntity<UserProfile>> hello() {
        return repo.getUserProfileById("sd3212asd12").map(ResponseEntity::ok);
    }
}
