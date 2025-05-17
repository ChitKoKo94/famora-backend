package com.backend.fambien.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@NoArgsConstructor
@DynamoDbBean
public class UserProfile {

    private String id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    // list famo
    // list event

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
}
