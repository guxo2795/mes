package com.wimir.bae.global.image.service;

import com.wimir.bae.global.exception.CustomRuntimeException;
import com.wimir.bae.global.image.dto.ImageDTO;
import com.wimir.bae.global.image.mapper.ImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageMapper imageMapper;

    @Value("${bae.path.images}")
    private String imageGlobalPath;
    private final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public String saveImage(MultipartFile file) {
        if(file.getSize() > MAX_FILE_SIZE) {
            throw new CustomRuntimeException("이미지 크기는 10MB를 넘을 수 없습니다.");
        }
        String fileName = file.getOriginalFilename();
        if(fileName == null || fileName.isEmpty()) {
            throw new CustomRuntimeException("이미지 파일 명은 비어있을 수 없습니다.");
        }
        if(!List.of("jpg", "jpeg", "png", "bmp")
                .contains(fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase())) {
            throw new CustomRuntimeException("이미지 확장자는 jpg, jpeg, png, bmp 중에서 가능합니다.");
        }
        String imageKey = String.valueOf(UUID.randomUUID());
        String imagePath = createImage(imageKey, fileName, file);

        ImageDTO imageDTO =
                ImageDTO.builder()
                        .imageKey(imageKey)
                        .imagePath(imagePath)
                        .build();
        imageMapper.saveImage(imageDTO);

        return imageKey;
    }

    private String createImage(String imageKey, String fileName, MultipartFile file) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String imagePath = imageGlobalPath + "/" + imageKey + "." + fileExtension;

        Path saveImagePath = Paths.get(imagePath);

        try {
            File directory = new File(imageGlobalPath);
            if(!directory.exists()) {
                directory.mkdirs();
            }
            Files.write(saveImagePath, file.getBytes());
        } catch (Exception e) {
            throw new CustomRuntimeException("리소스 저장 중 오류가 발생하였습니다.");
        }

        return imagePath;
    }
}
