package com.sawtooth.ahacentralserver.storage.repositories.chunk;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.models.chunk.ChunkMapper;
import com.sawtooth.ahacentralserver.models.storageserver.StorageServer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChunkRepository implements IChunkRepository {
    private JdbcTemplate template;

    @Override
    public void SetJbdcTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Chunk GetByName(String name) {
        return template.queryForObject("SELECT * FROM get_chunk_by_name(?)", new ChunkMapper(), name);
    }

    @Override
    public int Put(Chunk chunk) {
        return Objects.requireNonNull(template.queryForObject("SELECT * FROM put_chunk(?, ?, ?, ?)",
            new SingleColumnRowMapper<>(), chunk.fileID(), chunk.name(), chunk.size(), chunk.sequenceNumber()));
    }

    @Override
    public List<Chunk> GetByFile(int fileID) {
        return template.query("SELECT * FROM get_file_chunks(?)", new ChunkMapper(), fileID);
    }

    @Override
    public List<Chunk> GetByStorageServer(StorageServer server, int start, int count) {
        try {
            return template.query("SELECT * FROM get_chunks_of_storage_server(?, ?, ?)", new ChunkMapper(),
                server.storageServerID(), start, count);
        }
        catch (Exception exception) {
            return new ArrayList<>();
        }
    }

    @Override
    public void Delete(Chunk chunk) {
        template.queryForObject("SELECT * FROM delete_chunk(?)", new SingleColumnRowMapper<>(), chunk.chunkID());
    }

    @Override
    public long Count() {
        return Objects.requireNonNull(template.queryForObject("SELECT * FROM get_chunks_count()", Long.class));
    }
}
