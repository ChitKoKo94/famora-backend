package com.backend.fambien.service;

import com.backend.fambien.entity.UserProfile;
import reactor.core.publisher.Mono;

public interface UserProfileService {

    Mono<UserProfile> getProfileById(String id);

    Mono<UserProfile> createProfile(UserProfile profile);

    Mono<UserProfile> updateProfile(UserProfile profile);

    Mono<Void> updateUsername(UserProfile profile);
}
