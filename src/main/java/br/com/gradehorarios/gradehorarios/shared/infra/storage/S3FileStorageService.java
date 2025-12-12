package br.com.gradehorarios.gradehorarios.shared.infra.storage;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.gradehorarios.gradehorarios.shared.domain.service.FileStorageService;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3FileStorageService implements FileStorageService{

    @Autowired
    private S3Client s3Client;
    @Autowired
    private S3Presigner s3Presigner;

    @Value("${spring.storage.s3.bucket-name}")
    private String bucketName;


    @Override
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

    @Override
    public void deleteFile(String fileName) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
                throw new RuntimeException("Erro ao deletar arquivo do Storage: " + e.getMessage(), e);
            }
        }

    @Override
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
    
    @Override
    public String getPublicUrl(String fileName) {
        String baseUrl = s3Client.serviceClientConfiguration().endpointOverride().orElseThrow().toString();
        String publicBase = baseUrl.replace("/s3", "/object/public");
        
        return String.format("%s/%s/%s", publicBase, bucketName, fileName);
    }

    @Override
    public String getPresignedUrl(String fileName) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(60 * 12))
                    .getObjectRequest(getObjectRequest)
                    .build();

            return s3Presigner.presignGetObject(presignRequest).url().toString();
            
        } catch (S3Exception e) {
            throw new RuntimeException("Erro ao gerar URL assinada: " + e.getMessage(), e);
        }
    }

}