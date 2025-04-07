package com.wimir.bae.global.dto;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T> {

    private int result;

    private String message;

    private T data;
}
