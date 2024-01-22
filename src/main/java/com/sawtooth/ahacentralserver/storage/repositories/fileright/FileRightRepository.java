package com.sawtooth.ahacentralserver.storage.repositories.fileright;

import com.sawtooth.ahacentralserver.models.fileright.FileRight;
import com.sawtooth.ahacentralserver.models.fileright.FileRightMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class FileRightRepository implements IFileRightRepository {
    private JdbcTemplate template;

    @Override
    public void SetJbdcTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public FileRight Get(String name) {
        return template.queryForObject("SELECT * FROM get_file_right(?)", new FileRightMapper(), name);
    }

    @Override
    public List<FileRight> GetAll() {
        return template.query("SELECT * FROM get_file_rights()", new FileRightMapper());
    }
}
