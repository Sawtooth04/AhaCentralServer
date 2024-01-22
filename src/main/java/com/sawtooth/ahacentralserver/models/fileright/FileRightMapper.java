package com.sawtooth.ahacentralserver.models.fileright;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FileRightMapper implements RowMapper<FileRight> {
    @Override
    public FileRight mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FileRight(
            rs.getInt("fileRightID"),
            rs.getString("name")
        );
    }
}
