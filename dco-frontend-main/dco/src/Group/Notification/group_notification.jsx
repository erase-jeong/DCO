import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import ListItem from '../../assets/list_item';
import './group_notification.css';

import Loading from '../../Components/loading.jsx';

const GroupNotification = () => {
  const { groupId } = useParams();
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // 여기에 API 호출 코드를 추가하여 notifications 상태를 업데이트합니다.
    // 예시 데이터를 사용하겠습니다.
    const fetchNotifications = async () => {
      const data = [
        {
          notificationId: 1,
          title: '첫 번째 공지',
          date: '2023-10-05',
          author: '관리자',
        },
        {
          notificationId: 2,
          title: '두 번째 공지',
          date: '2023-10-06',
          author: '관리자',
        },
      ];
      setNotifications(data);
      setLoading(false);
    };

    fetchNotifications();
  }, [groupId]);

  if (loading) {
    return <Loading />;
  }

  return (
    <div className="notification-list-container">
      <Link to={`/group/${groupId}/notification/write`}>
        <button className="assignment-write-button">공지 생성</button>
      </Link>
      <div className="notification-list">
        <div
          className="notification-list-header"
          style={{ gridTemplateColumns: '6fr 2fr 2fr' }}
        >
          <span className="notification-list-title">제목</span>
          <span className="notification-list-date">작성일</span>
          <span className="notification-list-author">작성자</span>
        </div>
        {notifications.map((notification) => (
          <ListItem
            key={notification.notificationId}
            link={`/group/${groupId}/notification/${notification.notificationId}`}
            title={notification.title}
            templateColumns="6fr 2fr 2fr"
            columns={[
              {
                content: notification.date,
                className: 'notification-list-date',
              },
              {
                content: notification.author,
                className: 'notification-list-author',
              },
            ]}
          />
        ))}
      </div>
    </div>
  );
};

export default GroupNotification;
