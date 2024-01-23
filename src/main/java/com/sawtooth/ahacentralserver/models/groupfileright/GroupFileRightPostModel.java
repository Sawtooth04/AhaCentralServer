package com.sawtooth.ahacentralserver.models.groupfileright;

public record GroupFileRightPostModel(GroupFileRight groupFileRight, String fileName, String path) {
    public GroupFileRightPostModel WithPath(String path) {
        return new GroupFileRightPostModel(groupFileRight, fileName, path);
    }
}
