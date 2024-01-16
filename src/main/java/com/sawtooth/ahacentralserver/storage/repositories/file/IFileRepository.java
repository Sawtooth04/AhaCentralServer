package com.sawtooth.ahacentralserver.storage.repositories.file;

import com.sawtooth.ahacentralserver.models.file.DirectoryItem;
import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.storage.repositories.IRepository;

import java.util.List;

public interface IFileRepository extends IRepository {
    public int Put(File file);

    public File Get(String path, String name);

    public void Delete(File file);

    public List<DirectoryItem> GetFiles(String path);

    public List<DirectoryItem> GetDirectories(String path);
}
