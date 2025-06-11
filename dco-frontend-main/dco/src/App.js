import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './Common/header';
import Sidebar from './Common/sidebar';
import Footer from './Common/footer'; // 새로운 Footer 컴포넌트 추가

import Home from './Home/home_main';
import Login from './Login/login_main';
import Logout from './Logout/logout_main';
import Register from './Register/register_main';
import Mypage from './Mypage/mypage_main';
import Group from './Group/group_main';
import Grouplist from './Grouplist/grouplist_main';
import Groupcreation from './Grouplist/group_creation';
import GitHubRegister from './GitHub/github_registration';
import GitHubInfoForm from './GitHub/github_info_form';

import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import './padding.css';
import './App.css';
import { AuthProvider } from './AuthContext';

function App() {
  return (
    <div className="App">
      <AuthProvider>
        <Router>
          <Header />
          <div className="main-container">
            <div className="content">
              <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/logout" element={<Logout />} />
                <Route path="/register" element={<Register />} />
                <Route path="/mypage" element={<Mypage />} />
                <Route path="/githubRegister" element={<GitHubRegister />} />
                <Route path="/github" element={<GitHubInfoForm />} />
                <Route path="/grouplist" element={<Grouplist />} />
                <Route path="/grouplist/creation" element={<Groupcreation />} />
                <Route path="/group/:groupId/*" element={<Group />} />
              </Routes>
            </div>
          </div>
          <Footer />
        </Router>
      </AuthProvider>
      <ToastContainer
        position="top-right" // 알람 위치 지정
        autoClose={3000} // 자동 off 시간
        hideProgressBar={false} // 진행시간바 숨김
        closeOnClick // 클릭으로 알람 닫기
        rtl={false} // 알림 좌우 반전
        pauseOnFocusLoss // 화면을 벗어나면 알람 정지
        draggable // 드래그 가능
        // pauseOnHover // 마우스를 올리면 알람 정지
        theme="light"
        // limit={1} // 알람 개수 제한
      />
    </div>
  );
}

export default App;
