import React, { useState } from 'react';
import github from '../apis/github/fetchGithub';
import './GitHubInfoForm.css'; // CSS 파일 임포트
import { useNavigate } from 'react-router-dom';

const GithubRegistration = () => {
    const [showPopup, setShowPopup] = useState(false); // 팝업 상태
    const navigate = useNavigate();
    const [message, setMessage] = useState('');
    const [data, setData] = useState({});

    const handleSubmit = async (e) => {
        e.preventDefault(); // 폼 새로고침 방지

        try {
            const response = await github();
            if (response && response.data) {
                console.log('GitHub API 결과:', response.data);
                setData(response.data.data); // data 객체 설정
                setMessage(response.data.message); // message 설정
                setShowPopup(true);
            }
        } catch (error) {
            console.error('GitHub API 호출 중 오류 발생:', error);
            if (error.response) {
                console.error('서버 응답:', error.response.data);
                console.error('상태 코드:', error.response.status);
            } else if (error.request) {
                console.error('응답 없음:', error.request);
            } else {
                console.error('요청 설정 오류:', error.message);
            }
        }
    };

    const handlePopupClose = () => {
        setShowPopup(false);
        navigate('/mypage');
    };

    return (
        <div className="github-info-form">
            <h2>GitHub 정보 조회</h2>
            <form onSubmit={handleSubmit}>
                <button type="submit">정보 조회</button>
                {showPopup && (
                    <div className="popup-overlay">
                        <div className="popup">
                            <h3>깃허브 연결 성공</h3>
                            <p>{message}</p>
                            <p>GitHub Token: {data.githubToken}</p>
                            <p>GitHub Name: {data.githubName}</p>
                            <p>Repository Name: {data.repositoryName}</p>
                            <button onClick={handlePopupClose}>확인</button>
                        </div>
                    </div>
                )}
            </form>
        </div>
    );
};

export default GithubRegistration;