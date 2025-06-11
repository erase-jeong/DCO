package dco.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNotificationRequest {
    private Long notificationId;
    private String title;
    private String content;
    private Long groupId;
}
