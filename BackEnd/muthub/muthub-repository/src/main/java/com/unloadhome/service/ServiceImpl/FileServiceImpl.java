package com.unloadhome.service.ServiceImpl;

import com.unloadhome.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService{
    @Override
    public boolean addfile(String filePath, MultipartFile file) throws IOException {
        File path = new File(filePath);
        File parent = path.getParentFile();
        if(!parent.exists()){
            parent.mkdirs();
        }
        file.transferTo(path);
        return true;
    }
}
