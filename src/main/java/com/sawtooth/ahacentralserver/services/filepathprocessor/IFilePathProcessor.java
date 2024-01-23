package com.sawtooth.ahacentralserver.services.filepathprocessor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;

public interface IFilePathProcessor {
    public String ReplaceFilePathParts(String path);

    public String GetFilePath(HttpServletRequest request, String mapping);
}
