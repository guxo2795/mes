package com.wimir.bae.domain.notification.mapper;

import com.wimir.bae.domain.notification.dto.NotificationRegDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper {
    
    // 메시지 생성
    void createNotification(NotificationRegDTO notificationRegDTO);
}
