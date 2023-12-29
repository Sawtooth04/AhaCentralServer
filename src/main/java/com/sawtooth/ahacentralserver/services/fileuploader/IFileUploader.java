package com.sawtooth.ahacentralserver.services.fileuploader;

import com.sawtooth.ahacentralserver.models.file.FilePutModel;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface IFileUploader {
    public void Upload(FilePutModel model) throws IOException, InstantiationException, NoSuchAlgorithmException;
}
