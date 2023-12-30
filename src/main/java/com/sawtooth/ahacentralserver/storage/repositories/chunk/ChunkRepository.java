package com.sawtooth.ahacentralserver.storage.repositories.chunk;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

public class ChunkRepository implements IChunkRepository {
    private JdbcTemplate template;

    @Override
    public void SetJbdcTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void Put(Chunk chunk) {
        template.queryForObject("SELECT * FROM put_chunk(?, ?, ?, ?)", new SingleColumnRowMapper<>(), chunk.fileID(),
            chunk.name(), chunk.size(), chunk.sequenceNumber());
    }
}
