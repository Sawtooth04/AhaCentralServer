package com.sawtooth.ahacentralserver.services.filedeleter;

import com.sawtooth.ahacentralserver.models.file.File;

public interface IFileDeleter {
    public boolean Delete(File file) throws InstantiationException;
}
