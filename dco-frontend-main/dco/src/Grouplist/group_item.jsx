// src/components/GroupItem.jsx
import React from 'react';
import './group_item.css';
import GroupImgSample from '../assets/devWorks.jpg'; // 기본 이미지

const GroupItem = ({ title, professor, semester, imageUrl }) => {
  return (
    <div className="group-item-container">
      <div
        className="group-img-container"
        style={{ backgroundImage: `url(${imageUrl || GroupImgSample})` }}
      ></div>
      <div className="group-detail-container">
        <div className="GroupName">{title}</div>
        <div className="semester">{semester}</div>
        <div className="professor">{professor}</div>
      </div>
    </div>
  );
};

export default GroupItem;
