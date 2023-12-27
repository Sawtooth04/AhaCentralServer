package com.sawtooth.ahacentralserver.models.storageserverstatus;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StorageServerStatusMapper implements RowMapper<StorageServerStatus> {
    @Override
    public StorageServerStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new StorageServerStatus(
            rs.getInt("storageServerStatusID"),
            rs.getString("name")
        );
    }
}
