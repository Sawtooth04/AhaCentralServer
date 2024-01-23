package com.sawtooth.ahacentralserver.models.file;

import org.springframework.web.multipart.MultipartFile;

public record FileUploadModel(MultipartFile file, String path) {
    public FileUploadModel WithPath(String path) {
        return new FileUploadModel(file, path);
    }
}
