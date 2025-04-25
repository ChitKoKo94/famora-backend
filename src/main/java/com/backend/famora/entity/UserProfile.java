package com.backend.famora.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@NoArgsConstructor
@DynamoDbBean
public class UserProfile {

    private String id;
    private String firstName;
    private String lastName;
    private String userName;
    // list famo
    // list event

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
}
