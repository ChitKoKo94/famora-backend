package com.backend.fambien.entity;

import com.backend.fambien.enums.FamoType;
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
