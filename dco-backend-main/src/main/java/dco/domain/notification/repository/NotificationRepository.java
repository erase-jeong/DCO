package dco.domain.notification.repository;

import dco.domain.group.entity.Group;
import dco.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByGroupOrderByNotificationIdDesc(Group group);

    Optional<Notification> findByNotificationId(Long notificationId);
}
