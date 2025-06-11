package dco.domain.notification.entity;

import dco.domain.group.entity.Group;
import dco.domain.notification.dto.UpdateNotificationRequest;
import dco.domain.notification.dto.UploadNotificationRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long notificationId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Builder
    public Notification(String title, String content, Group group){
        this.title = title;
        this.content = content;
        this.group = group;
    }

    public static Notification createNotification(UploadNotificationRequest request, Group group){
        return Notification.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .group(group)
                .build();

    }

    public void updateNotification(UpdateNotificationRequest request){
        this.title = request.getTitle();
        this.content = request.getContent();
    }
}
