package com.amanecopse.restimg.domain.image.api;

import com.amanecopse.restimg.domain.image.dto.ImageUploadResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.amanecopse.restimg.domain.image.application.ImageService;
import com.amanecopse.restimg.global.common.response.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image/files")
@RequiredArgsConstructor
public class ImageApi {
    private final ImageService imageService;

    @GetMapping(value = "/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getByName(@PathVariable("imageName") String imageName) throws IOException {
        return imageService.getByName(imageName);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ImageUploadResponse>> save(@RequestPart MultipartFile[] files) {
        ImageUploadResponse result = imageService.save(files);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @DeleteMapping("/{imageName}")
    public ResponseEntity<Void> delete(@PathVariable("imageName") String imageName) throws IOException {
        imageService.delete(imageName);
        return ResponseEntity.ok().build();
    }
}