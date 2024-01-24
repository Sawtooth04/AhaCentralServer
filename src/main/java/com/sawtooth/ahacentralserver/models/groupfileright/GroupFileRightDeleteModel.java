package com.sawtooth.ahacentralserver.models.groupfileright;

public record GroupFileRightDeleteModel(GroupFileRight groupFileRight, String fileName, String path) {
    public GroupFileRightDeleteModel WithPath(String path) {
        return new GroupFileRightDeleteModel(groupFileRight, fileName, path);
    }
}
