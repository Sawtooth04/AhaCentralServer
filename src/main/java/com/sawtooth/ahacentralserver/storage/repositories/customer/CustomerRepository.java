package com.sawtooth.ahacentralserver.storage.repositories.customer;

import com.sawtooth.ahacentralserver.models.customer.Customer;
import com.sawtooth.ahacentralserver.models.customer.CustomerInfo;
import com.sawtooth.ahacentralserver.models.customer.CustomerInfoMapper;
import com.sawtooth.ahacentralserver.models.customer.CustomerMapper;
import com.sawtooth.ahacentralserver.models.file.File;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.util.List;
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
    public List<CustomerInfo> GetInfos() {
        return template.query("SELECT * FROM get_customer_infos()", new CustomerInfoMapper());
    }

    @Override
    public int Count() {
        return Objects.requireNonNull(template.queryForObject("SELECT * FROM get_customers_count()",
            new SingleColumnRowMapper<>()));
    }

    @Override
    public boolean IsCustomerHaveFileRight(Customer customer, File file, String fileRight) {
        return Boolean.TRUE.equals(template.queryForObject("SELECT * FROM is_customer_have_file_right(?, ?, ?)",
            new SingleColumnRowMapper<>(), customer.customerID(), file.fileID(), fileRight));
    }

    @Override
    public boolean IsCustomerHaveRole(Customer customer, String role) {
        return Boolean.TRUE.equals(template.queryForObject("SELECT * FROM is_customer_have_role(?, ?)",
            new SingleColumnRowMapper<>(), customer.customerID(), role));
    }

    @Override
    public void Update(Customer customer) {
        template.queryForObject("SELECT * FROM update_customer(?, ?, ?)", new SingleColumnRowMapper<>(), customer.customerID(),
            customer.name(), customer.passwordHash());
    }

    @Override
    public void Delete(String name) {
        template.queryForObject("SELECT * FROM delete_customer(?)", new SingleColumnRowMapper<>(), name);
    }
}
