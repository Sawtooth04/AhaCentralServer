package com.sawtooth.ahacentralserver.models.storageserver;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StorageServerMapper implements RowMapper<StorageServer> {
    @Override
    public StorageServer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new StorageServer(
            rs.getInt("storageServerID"),
            rs.getString("name"),
            rs.getString("address"),
            rs.getInt("storageServerStatusID")
        );
    }
}
