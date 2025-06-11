// Sidebar.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../AuthContext'; // useAuth 훅 임포트
import './sidebar.css';
import {
  FaHome,
  FaInfoCircle,
  FaUser,
  FaSignOutAlt,
  FaSignInAlt,
  FaTrophy,
} from 'react-icons/fa';

function Sidebar() {
  const { isLoggedIn, user } = useAuth();

  return (
    <div className="sidebar">
      {console.log(user)}
      <ul className="sidebar-menu">
        {/* {isLoggedIn ? (
          <li>
            <Link to="/logout" className="sidebar-link">
              <FaSignOutAlt className="sidebar-icon" />
              <span>로그아웃</span>
            </Link>
          </li>
        ) : (
          <li>
            <Link to="/login" className="sidebar-link">
              <FaSignInAlt className="sidebar-icon" />
              <span>로그인</span>
            </Link>
          </li>
        )} */}
      </ul>
    </div>
  );
}

export default Sidebar;
