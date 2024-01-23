package com.sawtooth.ahacentralserver.models.groupfileright;

public record GroupFileRight(int groupFileRightID, int fileID, int groupID, int fileRightID) {
    public GroupFileRight WithFileID(int fileID) {
        return new GroupFileRight(groupFileRightID, fileID, groupID, fileRightID);
    }
}
