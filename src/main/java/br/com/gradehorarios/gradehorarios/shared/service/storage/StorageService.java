package br.com.gradehorarios.gradehorarios.shared.service.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final S3Client s3Client;

    @Value("${spring.storage.s3.bucket-name}")
    private String bucketName;


    public String uploadFile(MultipartFile file, String fileName) {

        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putOb,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return fileName;
        } catch (IOException | S3Exception e) {
            throw new RuntimeException("Erro ao enviar arquivo para o Storage: " + e.getMessage(), e);
        }
    }


    public InputStream downloadFile(String fileName) {
        try {
            GetObjectRequest getOb = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            return s3Client.getObject(getOb, ResponseTransformer.toInputStream());
        } catch (S3Exception e) {
            throw new RuntimeException("Erro ao baixar arquivo do Storage: " + e.getMessage(), e);
        }
    }
    

    public String getPublicUrl(String fileName) {
        String baseUrl = s3Client.serviceClientConfiguration().endpointOverride().orElseThrow().toString();
        String publicBase = baseUrl.replace("/s3", "/object/public");
        
        return String.format("%s/%s/%s", publicBase, bucketName, fileName);
    }
}