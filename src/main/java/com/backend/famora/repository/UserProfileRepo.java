package com.backend.famora.repository;

import com.backend.famora.entity.UserProfile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class UserProfileRepo {
    private final DynamoDbAsyncTable<UserProfile> userProfileTable;

    public UserProfileRepo(DynamoDbEnhancedAsyncClient enhancedAsyncClient) {
        userProfileTable = enhancedAsyncClient.table("UserProfile", TableSchema.fromBean(UserProfile.class));
    }

    public Mono<UserProfile> getUserProfileById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return Mono.fromFuture(() -> userProfileTable.getItem(key));
    }
}
