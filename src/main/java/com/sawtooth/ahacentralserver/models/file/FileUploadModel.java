package com.sawtooth.ahacentralserver.models.file;

import org.springframework.web.multipart.MultipartFile;

public record FileUploadModel(MultipartFile file, String path) {
}
