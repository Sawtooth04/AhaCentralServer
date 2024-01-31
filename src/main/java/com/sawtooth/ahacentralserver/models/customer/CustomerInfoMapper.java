package com.sawtooth.ahacentralserver.models.customer;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerInfoMapper implements RowMapper<CustomerInfo> {
    @Override
    public CustomerInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CustomerInfo(
                rs.getString("name"),
                rs.getLong("filesCount"),
                rs.getLong("chunksCount"),
                rs.getLong("groupsCount")
        );
    }
}
