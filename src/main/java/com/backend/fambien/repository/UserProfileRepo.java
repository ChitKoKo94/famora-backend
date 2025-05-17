package com.backend.fambien.repository;

import com.backend.fambien.entity.UserProfile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.IgnoreNullsMode;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

@Repository
public class UserProfileRepo {
    private final DynamoDbAsyncTable<UserProfile> userProfileTable;
    private final DynamoDbEnhancedAsyncClient dynamoClient;

    public UserProfileRepo(DynamoDbEnhancedAsyncClient enhancedAsyncClient) {
        dynamoClient = enhancedAsyncClient;
        userProfileTable = enhancedAsyncClient.table("UserProfile", TableSchema.fromBean(UserProfile.class));
    }

    public Mono<UserProfile> getUserProfileById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return Mono.fromFuture(userProfileTable.getItem(key));
    }

    public Mono<UserProfile> getUserProfileByEmail(String email) {
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(email).build());
        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .build();
        DynamoDbAsyncIndex<UserProfile> emailIndex = userProfileTable.index("email-gsi");
        return Flux.from(emailIndex.query(request))
                .flatMap(page -> Flux.fromIterable(page.items()))
                .next();
    }

    public Mono<UserProfile> createUserProfile(UserProfile profile) {
        return Mono.fromFuture(userProfileTable.putItem(profile)).thenReturn(profile);
    }

    public Mono<UserProfile> updateUserProfile(UserProfile profile) {
        return Mono.fromFuture(
                    userProfileTable.updateItem(
                        r -> r.item(profile).conditionExpression(
                                Expression.builder()
                                        .expression("attribute_exists(id)")
                                        .build())))
                .thenReturn(profile)
                .onErrorResume(e -> {
                    if (e instanceof ConditionalCheckFailedException) {
                        return Mono.error(new RuntimeException("Item does not exist, update failed"));
                    }
                    return Mono.error(e);
                });
    }

    public Mono<Void> updateUserName(UserProfile profile) {
        UserProfile partial = new UserProfile();
        partial.setId(profile.getId());
        partial.setUserName(profile.getUserName());

        UpdateItemEnhancedRequest<UserProfile> req = UpdateItemEnhancedRequest.<UserProfile>builder(UserProfile.class)
                .item(partial)
                .ignoreNullsMode(IgnoreNullsMode.SCALAR_ONLY)
                .conditionExpression(
                        Expression.builder()
                            .expression("attribute_exists(id)")
                            .build())
                .build();

        return Mono.fromFuture(() -> userProfileTable.updateItem(req)).then();
    }

}
