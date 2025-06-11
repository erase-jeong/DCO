import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './styles.css';
import login from '../apis/user/login';
import { useAuth } from '../AuthContext';
import { jwtDecode } from 'jwt-decode';

const LoginMain = () => {
  const [code, setCode] = useState('');
  const [password, setPassword] = useState('');
  const [loginFailed, setLoginFailed] = useState(false);

  const navigate = useNavigate();
  const { login: authLogin } = useAuth();

  const handleCodeChange = (e) => {
    setCode(e.target.value);
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Code:', code);
    console.log('Password:', password);
  };

  const handleRegisterClick = () => {
    navigate('/register');
  };

  const handleLoginClick = async () => {
    try {
      const response = await login(code, password);
      const { accessToken, refreshToken, grantType } = response.data;

      console.log(accessToken);
      console.log(grantType);

      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);

      // JWT 토큰 디코딩
      const decodedToken = jwtDecode(accessToken); // 여기를 수정
      console.log('Decoded Token:', decodedToken); // 디코딩된 토큰을 출력하여 확인
      const userRole = decodedToken.auth;

      console.log('User Role:', userRole);

      // 필요한 경우 role을 로컬 스토리지에 저장
      localStorage.setItem('userRole', userRole);

      console.log('로그인 성공:', response);

      authLogin();
      navigate('/');
    } catch (error) {
      console.error('로그인 실패', error);
      setLoginFailed(true);
    }
  };

  return (
    <div>
      <div className="login-container">
        <h2>로그인</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <input
              id="code"
              value={code}
              onChange={handleCodeChange}
              placeholder="학번"
              required
            />
          </div>
          <div className="form-group">
            <input
              type="password"
              id="password"
              value={password}
              onChange={handlePasswordChange}
              placeholder="비밀번호"
              required
            />
          </div>
          <button
            type="submit"
            onClick={handleLoginClick}
            className="login-button"
          >
            로그인
          </button>
        </form>

        {loginFailed && (
          <div className="loginFailMsg">
            학번 또는 비밀번호가 잘못 되었습니다. 학번와 비밀번호를 정확히
            입력해 주세요.
          </div>
        )}

        <div className="registerClick-wrapper">
          <p>아직 dco에 가입하지 않았다면?</p>
          <button onClick={handleRegisterClick} className="register-button">
            회원가입
          </button>
        </div>
      </div>
    </div>
  );
};

export default LoginMain;
