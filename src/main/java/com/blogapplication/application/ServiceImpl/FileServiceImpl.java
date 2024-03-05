package com.blogapplication.application.ServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.blogapplication.application.Service.FileService;

@Component
public class FileServiceImpl implements FileService {

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        // TODO Auto-generated method stub

        String fullPath= path +File.separator + fileName;
        InputStream is = new FileInputStream((fullPath));




        return is;
    }

    @Override
    public String uploadImage(String path, MultipartFile fileName) throws IOException {
        

        //FILE NAME
        String name=fileName.getOriginalFilename();

        //RANDOM GENERATE FILE

        String randomID=UUID.randomUUID().toString();
        String filename1=randomID.concat(name.substring(name.lastIndexOf(".")));

        String filePath=path+ File.separator+ filename1;

        File f=new File(path);

        if(!f.exists())
        {
            f.mkdir();
        }



        Files.copy(fileName.getInputStream(), Paths.get(filePath));


        return filename1;
    }
    
}
