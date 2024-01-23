package com.sawtooth.ahacentralserver.services.filepathprocessor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerMapping;

@Service
public class FilePathProcessor implements IFilePathProcessor {
    @Override
    public String ReplaceFilePathParts(String path) {
        String result = path.replaceAll("(/[^/]*)$", "").replace("/root", "")
                .replace("root", "");
        return result.isEmpty() ? "/" : result.replaceAll("%20", " ");
    }

    @Override
    public String GetFilePath(HttpServletRequest request, String mapping) {
        String path = ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).replace(mapping, "");
        return ReplaceFilePathParts(path);
    }
}
