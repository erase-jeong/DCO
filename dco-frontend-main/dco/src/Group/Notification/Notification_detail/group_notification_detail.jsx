import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { Viewer } from '@toast-ui/react-editor';
import '@toast-ui/editor/dist/toastui-editor-viewer.css';
// import './group_assignment_detail.css';

const notification = {
  id: 1,
  title: '첫 번째 공지',
  uploadTime: '2024.07.01 오후 12:00',
  content: '첫 번째 공지 내용입니다.',
};

const GroupNotificationDetail = () => {
  const { groupId, notificationId } = useParams();
  console.log('groupId:', groupId);
  console.log('notificationId:', notificationId);

  if (!notification) {
    return <div>공지를 찾을 수 없습니다.</div>;
  }

  return (
    <div className="assignment-detail-container">
      <div className="assignment-container">
        <h2 className="assignment-title">{notification.title}</h2>
        <hr />
        <div className="assignment-row">
          <div className="assignment-label">
            <span className="assignment-label-name">공지 작성일</span>
            <span className="assignment-label-value">
              {notification.uploadTime}
            </span>
          </div>
        </div>
        <hr />
        <div className="assignment-content">
          <Viewer initialValue={notification.content} />
        </div>
      </div>
    </div>
  );
};

export default GroupNotificationDetail;
