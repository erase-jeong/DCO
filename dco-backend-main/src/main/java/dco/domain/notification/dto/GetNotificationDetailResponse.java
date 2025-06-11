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
public class GetNotificationDetailResponse {

    private Long notificationId;
    private String title;
    private String content;

    public static GetNotificationDetailResponse toDto(Notification notification){
        return GetNotificationDetailResponse.builder()
                .notificationId(notification.getNotificationId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .build();
    }
}
