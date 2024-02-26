package com.amanecopse.restimg.domain.image.dto;

import java.util.List;
import lombok.Data;

@Data
public class ImageUploadResponse {
    private List<String> fileNames;

    public ImageUploadResponse(List<String> fileNames){
        this.fileNames = fileNames;
    }
}
