package com.backend.fambien.controller;

import com.backend.fambien.entity.UserProfile;
import com.backend.fambien.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

@RestController
@RequestMapping("api/v1/user-profile")
@RequiredArgsConstructor
public class UserController {
    private final UserProfileService userProfileService;

    @PostMapping
    public Mono<ResponseEntity<UserProfile>> register(@RequestBody UserProfile profile) {
        return userProfileService.createProfile(profile)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("username")
    public Mono<?> updateUsername(@RequestBody UserProfile profile) {
        return userProfileService.updateUsername(profile)
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
        return userProfileService.getProfileById(id).map(ResponseEntity::ok);
    }

}
