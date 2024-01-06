package com.sawtooth.ahacentralserver.services.chunknamebuilder;

import com.sawtooth.ahacentralserver.models.file.FileUploadModel;

import java.security.NoSuchAlgorithmException;

public interface IChunkNameBuilder {
    public String GetChunkName(String path, String name, int chunkPointer) throws NoSuchAlgorithmException;
}
