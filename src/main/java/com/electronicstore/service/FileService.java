package com.electronicstore.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileService {
    String uploadFile(MultipartFile file,String path);

    InputStream getResource(String path,String name);
}
