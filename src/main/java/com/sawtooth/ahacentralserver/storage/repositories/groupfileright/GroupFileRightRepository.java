package com.sawtooth.ahacentralserver.storage.repositories.groupfileright;

import com.sawtooth.ahacentralserver.models.customer.Customer;
import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.models.groupfileright.GroupFileRight;
import com.sawtooth.ahacentralserver.models.groupfileright.GroupFileRightMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.util.List;

public class GroupFileRightRepository implements IGroupFileRightRepository {
    private JdbcTemplate template;

    @Override
    public void SetJbdcTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<GroupFileRight> Get(File file) {
        return template.query("SELECT * FROM get_group_file_right(?)", new GroupFileRightMapper(), file.fileID());
    }

    @Override
    public List<GroupFileRight> GetOfOwner(Customer customer, File file) {
        return template.query("SELECT * FROM get_group_file_rights_of_owner(?, ?)", new GroupFileRightMapper(),
            customer.customerID(), file.fileID());
    }

    @Override
    public void Add(GroupFileRight groupFileRight) {
        template.queryForObject("SELECT * FROM add_group_file_right(?, ?, ?)", new SingleColumnRowMapper<>(),
            groupFileRight.fileID(), groupFileRight.groupID(), groupFileRight.fileRightID());
    }

    @Override
    public void Delete(GroupFileRight groupFileRight) {
        template.queryForObject("SELECT * FROM delete_group_file_right(?, ?, ?)", new SingleColumnRowMapper<>(),
            groupFileRight.fileID(), groupFileRight.groupID(), groupFileRight.fileRightID());
    }

    @Override
    public boolean IsFileHaveGroupRights(File file) {
        return Boolean.TRUE.equals(template.queryForObject("SELECT * FROM is_file_have_group_rights(?)",
            new SingleColumnRowMapper<>(), file.fileID()));
    }
}
