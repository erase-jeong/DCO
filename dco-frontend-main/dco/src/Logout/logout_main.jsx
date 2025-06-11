import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import logout from '../apis/user/logout';
import { useAuth } from '../AuthContext';

const LogoutMain = () => {
  const navigate = useNavigate();
  const { logout: authLogout } = useAuth();

  const handleLogoutClick = async () => {
    console.log();
    try {
      await logout();
      authLogout();
      navigate('/');
    } catch (error) {
      console.error('로그아웃 실패:', error);
    }
  };

  return (
    <div>
      <button onClick={handleLogoutClick}>로그아웃</button>
    </div>
  );
};

export default LogoutMain;
