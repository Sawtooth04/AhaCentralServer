package com.sawtooth.ahacentralserver.storage.repositories.file;

import com.sawtooth.ahacentralserver.models.file.File;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.util.Objects;

public class FileRepository implements IFileRepository {
    private JdbcTemplate template;

    @Override
    public void SetJbdcTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public int Put(File file) {
        return Objects.requireNonNull(template.queryForObject("SELECT * FROM put_file(?, ?, ?)", new SingleColumnRowMapper<>(),
            file.ownerID(), file.name(), file.path()));
    }
}
