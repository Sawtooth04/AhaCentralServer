package com.sawtooth.ahacentralserver.storage.repositories.fileright;

import com.sawtooth.ahacentralserver.models.fileright.FileRight;
import com.sawtooth.ahacentralserver.storage.repositories.IRepository;

import java.util.List;

public interface IFileRightRepository extends IRepository {
    public FileRight Get(String name);

    public List<FileRight> GetAll();
}
