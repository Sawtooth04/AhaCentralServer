package com.sawtooth.ahacentralserver.models.file;

import java.sql.Timestamp;

public record File(int fileID, int ownerID, String name, String path, Timestamp uploadDate, Timestamp updateDate) {
}
