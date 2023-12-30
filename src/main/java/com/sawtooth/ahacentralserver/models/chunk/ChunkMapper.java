package com.sawtooth.ahacentralserver.models.chunk;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChunkMapper implements RowMapper<Chunk> {
    @Override
    public Chunk mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Chunk(
            rs.getInt("chunkID"),
            rs.getInt("fileID"),
            rs.getString("name"),
            rs.getInt("size"),
            rs.getInt("sequenceNumber")
        );
    }
}
