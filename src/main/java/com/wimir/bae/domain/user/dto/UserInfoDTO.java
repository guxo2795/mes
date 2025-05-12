package com.wimir.bae.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {

    private String userKey;

    private String userCode;

    private String userName;

    private String permissionTypeFlag;

    private String departmentKey;

    private String departmentName;

    private String positionKey;

    private String positionName;

    private String phoneNumber;

//    private String userImageKey;
//
//    private String userImagePath;
//
//    private String userSignatureKey;
//
//    private String userSignaturePath;

}
