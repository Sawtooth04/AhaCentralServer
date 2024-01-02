package com.sawtooth.ahacentralserver.services.fileresourcecomposer;

import com.sawtooth.ahacentralserver.models.file.File;

import java.io.IOException;

public interface IFileResourceComposer {
    public java.io.File Compose(File file) throws InstantiationException, IOException;
}
