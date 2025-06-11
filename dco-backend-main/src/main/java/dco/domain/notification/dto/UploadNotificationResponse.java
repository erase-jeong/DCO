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
public class UploadNotificationResponse {

    private String title;
    private String content;

    public static UploadNotificationResponse toDto(Notification notification){
        return UploadNotificationResponse.builder()
                .title(notification.getTitle())
                .content(notification.getContent())
                .build();
    }
}
