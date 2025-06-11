//import React from 'react';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import logout from '../apis/user/logout';
import { checkTokenValidity } from '../apis/user/auth';
import { getNewRefreshToken } from '../apis/user/refresh';
import './mypage_main.css';
import logo from '../assets/images/GitHub_icon.png';

const Mypage = () => {
  const navigate = useNavigate();
  const [userRole, setUserRole] = useState('');

  useEffect(() => {
    const storedUserRole = localStorage.getItem('userRole');
    setUserRole(storedUserRole);

    const handleTokenRefresh = async () => {
      const isTokenValid = checkTokenValidity();

      if (!isTokenValid) {
        try {
          const data = await getNewRefreshToken();
          console.log('mypage_main에서 refreshToken 함수 호출완료');
          localStorage.setItem('accessToken', data.accessToken);
        } catch (error) {
          console.error('토큰 갱신 실패:', error);
        }
      }
    };

    handleTokenRefresh();
  }, []);

  const handleLogoutClick = async () => {
    try {
      await logout();
      navigate('/login');
    } catch (error) {
      console.error('로그아웃 실패:', error);
    }
  };

  const handleRegisterGitHubClick = () => {
    navigate('/githubRegister');
  };

  const handleGitHubInfoClick = () => {
    navigate('/github');
  };

  return (
    <div className="mypage-main-container">
      <div className="myProfile">
        <h2>나의 정보</h2>
        <div className="Box">
          <div className="name-container">
            <p className="name">김학생</p>
            <div className="role-box">STUDENT</div>
          </div>

          <div className="studentId-container">
            <span className="label">학번</span>
            <span className="studentId">2001315</span>
          </div>
          <button className="gitHubInfoClick" onClick={handleRegisterGitHubClick}>
            <img src={logo} alt="Logo" className="header-logo" />
            깃허브 연결하기
          </button>
          <button className="gitHubInfoClick" onClick={handleGitHubInfoClick}>
            <img src={logo} alt="Logo" className="header-logo" />
            나의 깃허브 정보
          </button>
        </div>
      </div>
    </div>
  );
};

export default Mypage;
