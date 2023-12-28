package com.sawtooth.ahacentralserver.models.customer;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper implements RowMapper<Customer> {
    public Customer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Customer(
            resultSet.getInt("customerID"),
            resultSet.getString("name"),
            resultSet.getString("passwordHash")
        );
    }
}
