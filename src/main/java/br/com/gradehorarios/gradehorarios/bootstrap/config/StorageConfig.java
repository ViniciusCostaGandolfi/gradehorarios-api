package br.com.gradehorarios.gradehorarios.bootstrap.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutBucketPolicyRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

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

        @Value("${spring.storage.s3.bucket-name}")
        private String bucketName;


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

        @Bean
        public S3Presigner s3Presigner() {
                return S3Presigner.builder()
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

        @Bean
        public CommandLineRunner initializeBucket(S3Client s3Client) throws Exception {
                return args -> {
                try {
                        s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
                        System.out.println("Storage: Bucket '" + bucketName + "' verificado com sucesso.");
                } catch (NoSuchBucketException e) {
                        criarBucket(s3Client);
                } catch (S3Exception e) {
                        if (e.statusCode() == 404) {
                        criarBucket(s3Client);
                        } else {
                        System.err.println("Storage: Erro ao verificar bucket '" + bucketName + "': " + e.getMessage());
                        }
                }
                this.applyPublicPolicy(s3Client);
                };

        }

        private void applyPublicPolicy(S3Client s3Client) {
                try {
                        String policy = """
                                {
                                "Version": "2012-10-17",
                                "Statement": [
                                {
                                "Effect": "Allow",
                                "Principal": { "AWS": ["*"] },
                                "Action": ["s3:GetObject"],
                                "Resource": ["arn:aws:s3:::%s/*"]
                                }
                                ]
                                }
                                """.formatted(bucketName);

                        s3Client.putBucketPolicy(PutBucketPolicyRequest.builder()
                                .bucket(bucketName)
                                .policy(policy)
                                .build());
                        
                        System.out.println("Storage: Política de leitura pública (GET) aplicada.");
                        } catch (Exception e) {
                        System.err.println("Storage: Erro ao aplicar política pública: " + e.getMessage());
                        }
        }

        private void criarBucket(S3Client s3Client) {
                try {
                s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
                System.out.println("Storage: Bucket '" + bucketName + "' criado automaticamente.");
                } catch (S3Exception e) {
                System.err.println("Storage: Falha crítica ao criar o bucket '" + bucketName + "': " + e.getMessage());
                }
        }
}