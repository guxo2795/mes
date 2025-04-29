package com.wimir.bae.global.notification;

import com.wimir.bae.domain.notification.dto.NotificationRegDTO;
import com.wimir.bae.domain.notification.mapper.NotificationMapper;
import com.wimir.bae.domain.user.mapper.UserMapper;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationGlobalService {

    private final NotificationMapper notificationMapper;
    private final UserMapper userMapper;

    @Transactional
    public void createInfoNotification(String action, String actionTypeKey, String actionTarget, String actionBy) {

        String userKey = userMapper.getUserKeyByUserCode(actionBy);

        NotificationRegDTO notificationRegDTO = NotificationRegDTO.builder()
                .userKey(userKey)
                .actionTypeKey(actionTypeKey)
                .alertLevelFlag("I")
                .authRangeFlag("O")
                .build();

        String alertContent;
        switch (action) {
            case "insert":
                alertContent = actionBy + "님이 " + actionTarget + "을(를) 생성하였습니다.";
                notificationRegDTO.setAlertContent(alertContent);
                break;
            case "update":
                alertContent = actionBy + "님이 " + actionTarget + "을(를) 수정하였습니다.";
                notificationRegDTO.setAlertContent(alertContent);
                break;
            case "delete":
                alertContent = actionBy + "님이 " + actionTarget + "을(를) 삭제하였습니다.";
                notificationRegDTO.setAlertContent(alertContent);
                break;
            case "upload":
                alertContent = actionBy + "님이 " + actionTarget + "을(를) 업로드 하였습니다.";
                notificationRegDTO.setAlertContent(alertContent);
                break;
            default:
                throw new CustomRuntimeException("올바른 로그 타입을 확인할 수 없습니다.");
        }
        notificationMapper.createNotification(notificationRegDTO);
    }
}
