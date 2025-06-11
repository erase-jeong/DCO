import React from 'react';

const GroupNotification = () => {
  const notifications = [
    { id: 1, title: '첫 번째 공지', content: '첫 번째 공지 내용입니다.' },
    { id: 2, title: '두 번째 공지', content: '두 번째 공지 내용입니다.' },
  ];

  return (
    <div>
      <h2>질문 게시판</h2>
      <div>
        {notifications.map((notification) => (
          <div key={notification.id} style={{ border: '1px solid #ccc', padding: '10px', margin: '10px 0' }}>
            <h3>{notification.title}</h3>
            <p>{notification.content}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default GroupNotification;
