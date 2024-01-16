package com.sawtooth.ahacentralserver.models.file;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DirectoryFolderMapper implements RowMapper<DirectoryItem> {
    @Override
    public DirectoryItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DirectoryItem(rs.getString("name"), 0, "", null, null, false);
    }
}
