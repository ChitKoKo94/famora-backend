package com.backend.fambien.service.implementation;

import com.backend.fambien.entity.UserProfile;
import com.backend.fambien.repository.UserProfileRepo;
import com.backend.fambien.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
class UserServiceImpl implements UserService {
    private final UserProfileRepo profileRepo;

    UserServiceImpl(UserProfileRepo profileRepo) {
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
