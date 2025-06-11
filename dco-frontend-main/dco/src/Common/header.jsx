// src/components/Header.jsx
import React from 'react';
import { Link, useNavigate } from 'react-router-dom'; // Link import 추가
import logo from '../assets/DCO_logo.png';
import './header.css';
import { useAuth } from '../AuthContext';
import logout from '../apis/user/logout';

function Header() {
  const { isLoggedIn, logout: authLogout } = useAuth();
  const navigate = useNavigate();

  const handleLogoutClick = async () => {
    try {
      await logout();
      authLogout();
      navigate('/');
    } catch (error) {
      console.error('로그아웃 실패:', error);
    }
  };

  return (
    <header className="header">
      <div className="header-content">
        <Link to="/">
          <img src={logo} alt="Logo" className="header-dcologo" />
        </Link>
        <nav className="header-nav">
          <div className="header-nav-left">
            <ul>{/* 로고 바로 옆 (아직 내용 없음) */}</ul>
          </div>
          <div className="header-nav-right">
            <ul>
              {isLoggedIn && (
                <li>
                  <Link to="/grouplist" className="nav-link">
                    그룹
                  </Link>
                </li>
              )}
              {isLoggedIn && (
                <li>
                  <Link to="/mypage" className="nav-link">
                    마이페이지
                  </Link>
                </li>
              )}
              <li>
                <Link
                  to={isLoggedIn ? '/' : '/login'}
                  className={`auth-button ${isLoggedIn ? 'logout' : 'login'}`}
                  onClick={isLoggedIn ? handleLogoutClick : undefined}
                >
                  {isLoggedIn ? '로그아웃' : '로그인'}
                </Link>
              </li>
            </ul>
          </div>
        </nav>
      </div>
    </header>
  );
}

export default Header;
