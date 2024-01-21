package com.sawtooth.ahacentralserver.models.group;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMapper implements RowMapper<Group> {
    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Group(
            rs.getInt("groupID"),
            rs.getString("name"),
            rs.getInt("customersCount"),
            rs.getString("ownerName")
        );
    }
}
