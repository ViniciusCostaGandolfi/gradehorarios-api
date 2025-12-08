package br.com.gradehorarios.gradehorarios.application.service.storage;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {
    String uploadFile(MultipartFile file, String fileName);

    InputStream downloadFile(String fileName);

    String getPublicUrl(String fileName);

    String getPresignedUrl(String fileName);

    void deleteFile(String fileName);
}
