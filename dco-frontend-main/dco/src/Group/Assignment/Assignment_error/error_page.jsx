// src/apis/group/Assignment_error/error_page.jsx
import React from 'react';
import { Link, useParams } from 'react-router-dom';
import './error_page.css'; // 에러 페이지 전용 CSS

const ErrorPage = () => {
  const { groupId } = useParams(); // groupId 추출

  return (
    <div className="error-page-container">
      <h1 className="error-message">아직 열람 기간이 아닙니다.</h1>
      <p className="error-submessage">
        과제 열람 기간이 시작되면 다시 시도해주세요.
      </p>
      <Link to={`/group/${groupId}/assignment`} className="go-back-link">
        <button className="go-back-button">돌아가기</button>
      </Link>
    </div>
  );
};

export default ErrorPage;
