package com.sawtooth.ahacentralserver.services.fileupdater;

import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.models.file.FileUploadModel;

import java.io.IOException;

public interface IFileUpdater {
    public boolean Update(FileUploadModel model, File file) throws IOException, InstantiationException;
}
