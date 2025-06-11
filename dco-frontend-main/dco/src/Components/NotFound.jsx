// src/components/NotFound.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import './NotFound.css'; // 스타일링을 위한 CSS 파일

const NotFound = () => {
  return (
    <div className="notfound-container">
      <h1>404</h1>
      <h2>페이지를 찾을 수 없습니다.</h2>
      <p>요청하신 페이지가 존재하지 않거나, 사용할 수 없습니다.</p>
      <Link to="/" className="notfound-home-link">
        홈으로 돌아가기
      </Link>
    </div>
  );
};

export default NotFound;
