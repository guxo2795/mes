package com.wimir.bae.global.image.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDTO {

    private String imageKey;

    private String imagePath;
}
