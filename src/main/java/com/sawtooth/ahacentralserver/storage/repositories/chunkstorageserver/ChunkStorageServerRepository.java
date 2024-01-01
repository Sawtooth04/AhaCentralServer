package com.sawtooth.ahacentralserver.storage.repositories.chunkstorageserver;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.chunkstorageserver.ChunkStorageServer;
import com.sawtooth.ahacentralserver.models.chunkstorageserver.ChunkStorageServerMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.util.List;

public class ChunkStorageServerRepository implements IChunkStorageServerRepository {
    private JdbcTemplate template;

    @Override
    public void SetJbdcTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void Add(ChunkStorageServer chunkStorageServer) {
        template.queryForObject("SELECT * FROM add_chunk_storage_server(?, ?)", new SingleColumnRowMapper<>(),
            chunkStorageServer.chunkID(), chunkStorageServer.storageServerID());
    }

    @Override
    public List<ChunkStorageServer> GetByChunk(Chunk chunk) {
        return template.query("SELECT * FROM get_chunk_storage_server(?)", new ChunkStorageServerMapper(),
            chunk.chunkID());
    }
}
