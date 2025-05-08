package com.backend.famora.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;

@Configuration
public class DynamoDBConfig {
    @Value("${aws.dynamo.endPoint}")
    private String dbEndPoint;
    @Value("${aws.region}")
    private String awsRegion;
    @Value("${aws.dynamo.accessKey}")
    private String accessKey;
    @Value("${aws.dynamo.secret}")
    private String secretKey;

    @Bean
    public DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient() {
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(buildAsyncDynamoDbClient())
                .build();
    }

    private DynamoDbAsyncClient buildAsyncDynamoDbClient() {
        return DynamoDbAsyncClient.builder()
                .region(Region.of(awsRegion))
                .endpointOverride(URI.create(dbEndPoint))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();
    }
}
