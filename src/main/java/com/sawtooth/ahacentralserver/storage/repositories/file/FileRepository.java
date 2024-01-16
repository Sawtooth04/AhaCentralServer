package com.sawtooth.ahacentralserver.storage.repositories.file;

import com.sawtooth.ahacentralserver.models.file.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.util.List;
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

    @Override
    public List<DirectoryItem> GetFiles(String path) {
        return template.query("SELECT * FROM get_files(?)", new DirectoryFileMapper(), path);
    }

    @Override
    public List<DirectoryItem> GetDirectories(String path) {
        return template.query("SELECT * FROM get_directories(?)", new DirectoryFolderMapper(), path);
    }
}
