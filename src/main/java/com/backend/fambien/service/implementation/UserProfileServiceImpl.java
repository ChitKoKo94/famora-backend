package com.backend.fambien.service.implementation;

import com.backend.fambien.entity.UserProfile;
import com.backend.fambien.repository.UserProfileRepo;
import com.backend.fambien.service.UserProfileService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepo profileRepo;

    UserProfileServiceImpl(UserProfileRepo profileRepo) {
        this.profileRepo = profileRepo;
    }

    @Override
    public Mono<UserProfile> getProfileById(String id) {
        return profileRepo.getUserProfileById(id);
    }

    @Override
    public Mono<UserProfile> createProfile(UserProfile profile) {
        return profileRepo.createUserProfile(profile);
    }

    @Override
    public Mono<UserProfile> updateProfile(UserProfile profile) {
        return profileRepo.updateUserProfile(profile);
    }

    @Override
    public Mono<Void> updateUsername(UserProfile profile) {
        return profileRepo.updateUserName(profile);
    }
}
