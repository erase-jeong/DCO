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
public class GetNotificationResponse {
    private long notificationId;
    private String title;

    public static GetNotificationResponse toDto(Notification notification){
        return GetNotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .title(notification.getTitle())
                .build();
    }

}
