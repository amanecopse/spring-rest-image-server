package com.amanecopse.restimg.domain.image.application;

import com.amanecopse.restimg.domain.image.dto.ImageUploadResponse;
import com.amanecopse.restimg.global.error.FileUploadException;
import com.amanecopse.restimg.global.error.NotFoundException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ImageService {
    @Value("${file-upload-path}")
    private String uploadPath;

    public byte[] getByName(String imageName) {
        String path = getFilePath(imageName).toString();
        byte[] imageBytes;
        try {
            InputStream in = new FileSystemResource(path).getInputStream();
            imageBytes = IOUtils.toByteArray(in);
        } catch (IOException e) {
            throw new NotFoundException();
        }
        return imageBytes;
    }

    public ImageUploadResponse save(MultipartFile[] files) {
        List<String> fileNames = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
                String fileName = "%s_%s".formatted(dateFormat.format(new Date()), file.getOriginalFilename());
                fileNames.add(fileName);
                Path filePath = getFilePath(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new FileUploadException();
        }
        return new ImageUploadResponse(fileNames);
    }

    public void delete(String fileName) {
        Path filePath = getFilePath(fileName);
        File file = new File(filePath.toString());
        if (!file.exists()) {
            throw new NotFoundException();
        }
        if (!file.delete()) {
            throw new RuntimeException();
        }
    }

    private Path getFilePath(String imageName) {
        return Paths.get(uploadPath).resolve(imageName);
    }
}
