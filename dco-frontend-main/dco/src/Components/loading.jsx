import React from 'react';
import { ClipLoader } from 'react-spinners';
import './loading.css';

function Loading() {
  return (
    <div className="loading-container">
      <ClipLoader color="#3498db" size={60} />
      <p>로딩 중...</p>
    </div>
  );
}

export default Loading;
