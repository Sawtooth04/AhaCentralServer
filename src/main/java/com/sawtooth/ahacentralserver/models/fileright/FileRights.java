package com.sawtooth.ahacentralserver.models.fileright;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class FileRights extends RepresentationModel<FileRights> {
    public List<FileRight> fileRights;
}
