package com.sawtooth.ahacentralserver.storage.repositories.customer;

import com.sawtooth.ahacentralserver.models.customer.Customer;
import com.sawtooth.ahacentralserver.models.customer.CustomerMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.util.Objects;

public class CustomerRepository implements ICustomerRepository {
    private JdbcTemplate template;

    @Override
    public void SetJbdcTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public boolean IsCustomerNameFree(String name) {
        return Boolean.TRUE.equals(template.queryForObject("SELECT * FROM is_customer_name_free(?)",
            new SingleColumnRowMapper<>(), name));
    }

    @Override
    public void Add(Customer customer) {
        template.queryForObject("SELECT * FROM add_customer(?, ?)", new SingleColumnRowMapper<>(), customer.name(),
            customer.passwordHash());
    }

    @Override
    public Customer Get(String name) {
        return template.queryForObject("SELECT * FROM get_customer(?)", new CustomerMapper(), name);
    }

    @Override
    public int Count() {
        return Objects.requireNonNull(template.queryForObject("SELECT * FROM get_customers_count()",
            new SingleColumnRowMapper<>()));
    }
}
