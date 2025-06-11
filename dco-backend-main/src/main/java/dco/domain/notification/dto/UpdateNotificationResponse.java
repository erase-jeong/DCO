package dco.domain.notification.dto;

import dco.domain.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNotificationResponse {

    private Long NotificationId;
    private String title;
    private String content;

    public static UpdateNotificationResponse toDto(Notification notification){
        return UpdateNotificationResponse.builder()
                .NotificationId(notification.getNotificationId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .build();
    }
}

