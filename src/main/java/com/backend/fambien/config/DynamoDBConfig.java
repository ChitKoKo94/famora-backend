package com.backend.fambien.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;

@Configuration
public class DynamoDBConfig {
    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient(DynamoDbAsyncClient dynamoDbAsyncClient) {
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(dynamoDbAsyncClient)
                .build();
    }

    @Bean
    @Profile("dev")
    public DynamoDbAsyncClient buildAsyncDynamoDbClientDev(@Value("${aws.dynamo.endPoint}") String dbEndPoint,
                                                           @Value("${aws.dynamo.accessKey}") String accessKey,
                                                           @Value("${aws.dynamo.secret}") String secretKey) {
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

    @Bean
    @Profile("prod")
    public DynamoDbAsyncClient buildAsyncDynamoDbClientProd() {
        return DynamoDbAsyncClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(DynamoDbAsyncClient.class)
    public DynamoDbAsyncClient fallbackClient() {
        return DynamoDbAsyncClient.builder()
                .region(Region.of(awsRegion))
                .build();
    }
}
