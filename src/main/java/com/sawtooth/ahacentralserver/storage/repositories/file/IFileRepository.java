package com.sawtooth.ahacentralserver.storage.repositories.file;

import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.storage.repositories.IRepository;

public interface IFileRepository extends IRepository {
    public int Put(File file);

    public File Get(String path, String name);
}
