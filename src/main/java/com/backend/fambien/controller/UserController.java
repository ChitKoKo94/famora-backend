package com.backend.fambien.controller;

import com.backend.fambien.entity.UserProfile;
import com.backend.fambien.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

@RestController
@RequestMapping("api/v1/user-profile")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Mono<ResponseEntity<UserProfile>> register(@RequestBody UserProfile profile) {
        return userService.createProfile(profile)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("username")
    public Mono<?> updateUsername(@RequestBody UserProfile profile) {
        return userService.updateUsername(profile)
                .thenReturn(ResponseEntity.ok().build())
                .onErrorResume(ex -> {
                    if (ex instanceof ConditionalCheckFailedException) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @GetMapping
    public Mono<ResponseEntity<UserProfile>> getProfileById(@RequestParam String id) {
        return userService.getProfileById(id).map(ResponseEntity::ok);
    }

}
