package br.com.gradehorarios.api.shared.domain.service;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String uploadFile(MultipartFile file, String fileName);

    InputStream downloadFile(String fileName);

    String getPublicUrl(String fileName);

    String getPresignedUrl(String fileName);

    void deleteFile(String fileName);
}
