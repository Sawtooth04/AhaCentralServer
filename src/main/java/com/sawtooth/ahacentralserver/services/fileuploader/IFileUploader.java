package com.sawtooth.ahacentralserver.services.fileuploader;

import com.sawtooth.ahacentralserver.models.file.FileUploadModel;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface IFileUploader {
    public boolean Upload(FileUploadModel model, int customerID) throws IOException, InstantiationException, NoSuchAlgorithmException;
}
