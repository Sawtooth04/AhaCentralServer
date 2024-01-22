package com.sawtooth.ahacentralserver.storage.repositories.group;

import com.sawtooth.ahacentralserver.models.customer.Customer;
import com.sawtooth.ahacentralserver.models.group.Group;
import com.sawtooth.ahacentralserver.models.group.GroupMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.util.List;

public class GroupRepository implements IGroupRepository {
    private JdbcTemplate template;

    @Override
    public void SetJbdcTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Group Get(int groupID) {
        return template.queryForObject("SELECT * FROM get_group(?)", new GroupMapper(), groupID);
    }

    @Override
    public List<Group> Get(Customer customer) {
        return template.query("SELECT * FROM get_groups(?)", new GroupMapper(), customer.customerID());
    }

    @Override
    public List<Group> GetOwn(Customer customer) {
        return template.query("SELECT * FROM get_own_groups(?)", new GroupMapper(), customer.customerID());
    }

    @Override
    public void Add(Customer customer, Group group) {
        template.queryForObject("SELECT * FROM add_group(?, ?)", new SingleColumnRowMapper<>(), customer.customerID(), group.name());
    }

    @Override
    public void Update(Group group) {
        template.queryForObject("SELECT * FROM update_group(?, ?)", new SingleColumnRowMapper<>(), group.groupID(), group.name());
    }

    @Override
    public void Delete(Group group) {
        template.queryForObject("SELECT * FROM delete_group(?)", new SingleColumnRowMapper<>(), group.groupID());
    }
}
