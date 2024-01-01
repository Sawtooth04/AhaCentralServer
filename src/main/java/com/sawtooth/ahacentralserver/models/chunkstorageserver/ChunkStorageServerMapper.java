package com.sawtooth.ahacentralserver.models.chunkstorageserver;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChunkStorageServerMapper implements RowMapper<ChunkStorageServer> {
    @Override
    public ChunkStorageServer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ChunkStorageServer(
            rs.getInt("chunkStorageServerID"),
            rs.getInt("chunkID"),
            rs.getInt("storageServerID")
        );
    }
}
