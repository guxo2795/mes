package com.wimir.bae.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginInfoDTO {

    private String userCode;

    private String userName;

    private String departmentName;

    private String positionName;

    private String permissionTypeFlag;

    private String phoneNumber;

//    private String userImageKey;
//
//    private String userImagePath;
//
//    private String userSignatureKey;
//
//    private String userSignaturePath;

    private String tokenDate;
}
