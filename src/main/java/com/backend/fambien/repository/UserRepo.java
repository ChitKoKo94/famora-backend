package com.backend.fambien.repository;

import com.backend.fambien.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.concurrent.CompletableFuture;

@Repository
@Slf4j
public class UserRepo {
    private final DynamoDbAsyncTable<User> userTable;

    public UserRepo(DynamoDbEnhancedAsyncClient enhancedAsyncClient) {
        userTable = enhancedAsyncClient.table("User", TableSchema.fromBean(User.class));
    }

    /**
     * Finds a user by their username.
     *
     * @param username The username to find by.
     * @return A Mono emitting the User if found, an empty Mono otherwise.
     */
    public Mono<User> findByUsername(String username) {
        Key key = Key.builder()
                .partitionValue(username)
                .build();
        return Mono.fromFuture(userTable.getItem(key))
                .onErrorResume(e -> Mono.empty());
    }

    /**
     * Saves a user to DynamoDB.
     *
     * @param user The User object to save.
     * @return A Mono emitting the saved User.
     */
    public Mono<User> save(User user) {
        return Mono.fromFuture(userTable.putItem(user))
                .thenReturn(user)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to save user", e)));
    }
}
