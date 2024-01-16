package com.sawtooth.ahacentralserver.models.file;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DirectoryFileMapper implements RowMapper<DirectoryItem> {
    @Override
    public DirectoryItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DirectoryItem(
            rs.getString("name"),
            rs.getLong("size"),
            rs.getString("extension"),
            rs.getTimestamp("uploadDate"),
            rs.getTimestamp("updateDate"),
            true
        );
    }
}
