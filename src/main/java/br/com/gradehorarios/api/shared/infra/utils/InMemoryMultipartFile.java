package br.com.gradehorarios.api.shared.infra.utils;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InMemoryMultipartFile implements MultipartFile {

    private final String name;
    private final String originalFilename;
    private final String contentType;
    private final byte[] content;


    public InMemoryMultipartFile(
            String name,
            @Nullable String originalFilename,
            @Nullable String contentType,
            @Nullable byte[] content) {
        
        this.name = (name != null) ? name : "file";
        this.originalFilename = (originalFilename != null) ? originalFilename : "";
        this.contentType = (contentType != null) ? contentType : "application/octet-stream";
        this.content = (content != null) ? content : new byte[0];
    }



    @Override
    public boolean isEmpty() {
        return this.content.length == 0;
    }

    @Override
    public long getSize() {
        return this.content.length;
    }

    @Override
    @NonNull
    public byte[] getBytes() throws IOException {
        return this.content;
    }

    @Override
    @NonNull
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    @Override
    public void transferTo(@NonNull File dest) throws IOException, IllegalStateException {
        try (OutputStream os = new FileOutputStream(dest)) {
            os.write(this.content);
        }
    }
}