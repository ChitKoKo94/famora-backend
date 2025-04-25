package com.backend.famora.entity;

import com.backend.famora.enums.FamoType;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@Data
@DynamoDbBean
public class Famo {

    private String id;
    private String title;
    private List<String> members;
    private FamoType type;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
}
