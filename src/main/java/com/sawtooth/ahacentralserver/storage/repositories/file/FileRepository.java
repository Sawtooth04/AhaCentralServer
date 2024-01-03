package com.sawtooth.ahacentralserver.storage.repositories.file;

import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.models.file.FileMapper;
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

    @Override
    public File Get(String path, String name) {
        return template.queryForObject("SELECT * FROM get_file(?, ?)", new FileMapper(), path, name);
    }

    @Override
    public void Delete(File file) {
        template.queryForObject("SELECT * FROM delete_file(?)", new SingleColumnRowMapper<>(), file.fileID());
    }
}
