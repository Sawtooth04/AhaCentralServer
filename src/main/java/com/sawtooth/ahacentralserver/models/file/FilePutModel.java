package com.sawtooth.ahacentralserver.models.file;

import org.springframework.web.multipart.MultipartFile;

public record FilePutModel(MultipartFile file, String path) {
}
