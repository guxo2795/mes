package com.wimir.bae.domain.notification.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
public class NotificationRegDTO {

    private String userKey;

    @NotBlank(message = "알림유형은 비어있을 수 없습니다.")
    private String actionTypeKey;

    @Size(max = 1000, message = "알림내용은 1000자 이내로 입력가능 합니다.")
    private String alertContent;

    @NotBlank(message = "중요도 플래그는 비어있을 수 없습니다.")
    @Size(max = 1, message = "올바른 중요도 플래그가 아닙니다.")
    @Pattern(regexp = "^[AIW]$", message = "올바른 중요도 플래그가 아닙니다.")
    private String alertLevelFlag;

    @NotBlank(message = "공개범위 플래그는 비어있을 수 없습니다.")
    @Size(max = 1, message = "올바른 공개범위 플래그가 아닙니다.")
    @Pattern(regexp = "^[ACO]$", message = "올바른 공개범위 플래그가 아닙니다.")
    private String authRangeFlag;
}
