package com.sawtooth.ahacentralserver.storage.repositories.chunk;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.storage.repositories.IRepository;

import java.util.List;

public interface IChunkRepository extends IRepository {
    public Chunk GetByName(String name);

    public int Put(Chunk chunk);

    public List<Chunk> GetByFile(int fileID);

    public void Delete(Chunk chunk);
}
