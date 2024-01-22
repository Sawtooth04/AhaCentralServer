package com.sawtooth.ahacentralserver.models.groupfileright;

public class GroupFileRight {
    public int groupFileRightID, fileID,  groupID, fileRightID;

    public GroupFileRight() {
        groupFileRightID = fileID = groupID = fileRightID = 0;
    }

    public GroupFileRight(int groupFileRightID, int fileID, int groupID, int fileRightID) {
        this.groupFileRightID = groupFileRightID;
        this.fileID = fileID;
        this.groupID = groupID;
        this.fileRightID = fileRightID;
    }
}
