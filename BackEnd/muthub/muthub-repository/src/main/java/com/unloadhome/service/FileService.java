package com.unloadhome.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    public boolean addfile(String filePath, MultipartFile file) throws IOException;
}
