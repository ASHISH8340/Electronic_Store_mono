package com.electronicstore.service.serviceimpl;

import com.electronicstore.exceptions.BadApiRequest;
import com.electronicstore.exceptions.IdNotFoundException;
import com.electronicstore.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadFile(MultipartFile file, String path) {
        try{
            String originalFilename = file.getOriginalFilename();
            logger.info("Filename: {}",originalFilename);
            String filename = UUID.randomUUID().toString();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileNameWithExtension = filename + extension;
            String fullPathWithFileName = path + fileNameWithExtension;
            if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")){
                //File Save
                File folder = new File(path);
                if(!folder.exists()){
                    //create Folder
                    folder.mkdirs();
                }

                //upload

                Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
                return fileNameWithExtension;
            }else{
                throw new BadApiRequest("File With this "+extension+" not allowed !!");
            }



        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in uploadFile of FileServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }

    }

    @Override
    public InputStream getResource(String path, String name) {
        try{
            String fullPath = path + File.separator + name;
            InputStream inputStream =new FileInputStream(fullPath);
            return inputStream;
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in getResource of FileServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }

    }
}
