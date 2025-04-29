package com.wimir.bae.global.image.mapper;

import com.wimir.bae.global.image.dto.ImageDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageMapper {
    void saveImage(ImageDTO imageDTO);
}
