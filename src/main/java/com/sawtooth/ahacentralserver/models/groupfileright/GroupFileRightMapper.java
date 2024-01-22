package com.sawtooth.ahacentralserver.models.groupfileright;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupFileRightMapper implements RowMapper<GroupFileRight> {
    @Override
    public GroupFileRight mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new GroupFileRight(
            rs.getInt("groupFileRightID"),
            rs.getInt("fileID"),
            rs.getInt("groupID"),
            rs.getInt("fileRightID")
        );
    }
}
