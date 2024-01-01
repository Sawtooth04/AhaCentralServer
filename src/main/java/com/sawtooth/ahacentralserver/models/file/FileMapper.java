package com.sawtooth.ahacentralserver.models.file;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FileMapper implements RowMapper<File> {
    @Override
    public File mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new File(
            rs.getInt("fileID"),
            rs.getInt("ownerID"),
            rs.getString("name"),
            rs.getString("path"),
            rs.getTimestamp("uploadDate"),
            rs.getTimestamp("updateDate")
        );
    }
}
