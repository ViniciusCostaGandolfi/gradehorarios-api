package br.com.gradehorarios.gradehorarios.bootstrap.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class StorageConfig {

        @Value("${spring.storage.s3.endpoint}")
        private String endpoint;

        @Value("${spring.storage.s3.access-key}")
        private String accessKey;

        @Value("${spring.storage.s3.secret-key}")
        private String secretKey;

        @Value("${spring.storage.s3.region}")
        private String region;



        @Bean
        public S3Client s3Client() {
                return S3Client.builder()
                        .endpointOverride(URI.create(endpoint))
                        .credentialsProvider(StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        ))
                        .serviceConfiguration(S3Configuration.builder()
                                .pathStyleAccessEnabled(true) 
                                .build()
                        )
                        .region(Region.of(region))
                        .build();
        }
}