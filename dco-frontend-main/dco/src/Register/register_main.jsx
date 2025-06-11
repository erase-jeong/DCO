import React, { useState } from 'react';
import './styles.css';
import { useNavigate } from 'react-router-dom';
import signUp from '../apis/user/signUp';

const RegisterMain = () => {
  const navigate = useNavigate();

  const [code, setCode] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [role, setRole] = useState('');
  const [name, setName] = useState('');
  const [showPopup, setShowPopup] = useState(false); // 팝업 상태 추가

  const handleCodeChange = (e) => {
    setCode(e.target.value);
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };

  const handleConfirmPasswordChange = (e) => {
    setConfirmPassword(e.target.value);
  };

  const handleRoleChange = (e) => {
    setRole(e.target.value);
  };

  const handleNameChange = (e) => {
    setName(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMessage('');

    // 비밀번호 확인
    if (password !== confirmPassword) {
      setErrorMessage('비밀번호가 일치하지 않습니다.');
      return;
    }

    try {
      const result = await signUp(code, password, role, name);
      console.log('Sign Up Result:', result);
      setShowPopup(true); // 회원가입 성공 시 팝업 표시
    } catch (error) {
      console.error('Sign Up Error:', error);
      alert('회원가입이 실패했습니다.');
    }
  };

  const handlePopupClose = () => {
    setShowPopup(false);
    navigate('/login'); // 확인 버튼 누르면 로그인 창으로 이동
  };

  return (
    <div>
      <div className="register-container">
        <h2>회원가입</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="code">학번</label>
            <input
              id="code"
              value={code}
              onChange={handleCodeChange}
              placeholder=""
              required
            />
            <p className="notice-text"> (숫자 7자리)</p>
          </div>

          <div className="form-group">
            <label htmlFor="password">비밀번호</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={handlePasswordChange}
              placeholder=""
              required
            />
            <p className="notice-text">
              {' '}
              (영문 대소문자/숫자/특수문자 중 각각 1개 이상 포함, 8자이상)
            </p>
          </div>

          <div className="form-group">
            <label htmlFor="confirmPassword">비밀번호 확인</label>
            <input
              type="password"
              id="confirmPassword"
              value={confirmPassword}
              onChange={handleConfirmPasswordChange}
              placeholder=""
              required
            />
            <p className="notice-text">
              {errorMessage && <p className="error-message">{errorMessage}</p>}{' '}
            </p>
          </div>

          <div className="form-group">
            <label htmlFor="name">이름</label>
            <input
              id="name"
              value={name}
              onChange={handleNameChange}
              placeholder="이름"
              required
            />
            <p className="notice-text"></p>
          </div>

          <div className="form-group">
            <label htmlFor="role">구분</label>
            <select id="role" value={role} onChange={handleRoleChange} required>
              <option value="">선택해주세요</option>
              <option value="STUDENT">STUDENT</option>
              <option value="PROFESSOR">PROFESSOR</option>
              <option value="ADMIN">ADMIN</option>
            </select>
            <p className="notice-text"></p>
          </div>

          <div className="registerBtn-wrapper">
            <button type="submit" className="register-btn">
              가입하기
            </button>
          </div>
        </form>
      </div>

      {showPopup && (
        <div className="popup-overlay">
          <div className="popup">
            <h3>회원가입 성공</h3>
            <p>회원가입이 완료되었습니다.</p>
            <button onClick={handlePopupClose}>확인</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default RegisterMain;

/*
import React, { useState } from 'react';
import './styles.css';
import { useNavigate } from 'react-router-dom';
import signUp from '../apis/signUp';

const RegisterMain = () => {
    const navigate = useNavigate();

    const [code, setCode] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [errorMessage,setErrorMessage]=useState('');
    const [role, setRole] = useState('');
    const [name, setName] = useState('');

    const handleCodeChange = (e) => {
        setCode(e.target.value);
    };

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    };

    const handleConfirmPasswordChange = (e) => {
        setConfirmPassword(e.target.value);
    };

    const handleRoleChange = (e) => {
        setRole(e.target.value);
    };

    const handleNameChange = (e) => {
        setName(e.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage(''); 
        // 입력된 정보를 출력
        console.log('code:', code);
        console.log('Password:', password);
        console.log('Confirm Password:', confirmPassword);
        console.log('role:', role);
        console.log('name:', name);

        // 비밀번호 확인
        if (password !== confirmPassword) {
            setErrorMessage('비밀번호가 일치하지 않습니다.');
            return;
        }

        try {
            const result = await signUp(code, password, role, name);
            console.log('Sign Up Result:', result);
            console.log('회원가입이 완료되었습니다.');
            navigate("/login");
        } catch (error) {
            console.error('Sign Up Error:', error);
            alert('회원가입이 실패했습니다.');
        }
    };

    return (
        <div>
            <div className="register-container">
                <h2>회원가입</h2>
                <form onSubmit={handleSubmit}>

                    <div className="form-group">
                        <label htmlFor="code">학번</label>
                        <input
                            id="code"
                            value={code}
                            onChange={handleCodeChange}
                            placeholder=''
                            required
                        />
                        <p className="notice-text"> (숫자 7자리)</p>
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">비밀번호</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={handlePasswordChange}
                            placeholder=''
                            required
                        />
                        <p className="notice-text"> (영문 대소문자/숫자/특수문자 중 각각 1개 이상 포함, 8자이상)</p>
                    </div>

                    <div className="form-group">
                        <label htmlFor="confirmPassword">비밀번호 확인</label>
                        <input
                            type="password"
                            id="confirmPassword"
                            value={confirmPassword}
                            onChange={handleConfirmPasswordChange}
                            placeholder=''
                            required
                        />
                        <p className="notice-text">{errorMessage && <p className="error-message">{errorMessage}</p>}  </p>
                    </div>


                    <div className="form-group">
                        <label htmlFor="name">이름</label>
                        <input
                            id="name"
                            value={name}
                            onChange={handleNameChange}
                            placeholder='이름'
                            required
                        />
                        <p className="notice-text"></p>
                    </div>


                    <div className="form-group">
                        <label htmlFor="role">구분</label>
                        <select
                            id="role"
                            value={role}
                            onChange={handleRoleChange}
                            required
                        >
                            <option value="">선택해주세요</option>
                            <option value="STUDENT">STUDENT</option>
                            <option value="PROFESSOR">PROFESSOR</option>
                            <option value="ADMIN">ADMIN</option>
                        </select>
                        <p className="notice-text"></p>
                    </div>
                    
                    <div className="registerBtn-wrapper">
                        <button type="submit" className="register-btn">가입하기</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default RegisterMain;

*/
