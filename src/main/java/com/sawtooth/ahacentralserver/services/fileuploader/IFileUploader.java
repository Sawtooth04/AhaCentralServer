package com.sawtooth.ahacentralserver.services.fileuploader;

import com.sawtooth.ahacentralserver.models.file.FilePutModel;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface IFileUploader {
    public void Upload(FilePutModel model, int fileID) throws IOException, InstantiationException, NoSuchAlgorithmException;
}
