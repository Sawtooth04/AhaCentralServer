package com.sawtooth.ahacentralserver.models.file;

import java.sql.Timestamp;

public record DirectoryItem(String name, long size, String extension, Timestamp uploadDate, Timestamp updateDate, boolean isFile) {
}
