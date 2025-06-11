import React, { useState } from 'react';
import github from '../apis/github/registerGithub';
import './GitHubInfoForm.css'; // CSS 파일 임포트
//import { Navigate } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

const Github_registration = () => {
    const [githubToken, setGithubToken] = useState('');
    const [githubName, setGithubName] = useState('');
    const [repositoryName, setRepositoryName] = useState('');
    const [showPopup,setShowPopup]=useState(false); //팝업 상태
    const Navigate = useNavigate();
    const [message,setMessage]=useState('');


    const handleGithubToken = (e) => {
        setGithubToken(e.target.value);
    }

    const handleGithubName = (e) => {
        setGithubName(e.target.value);
    }

    const handleRepositoryName = (e) => {
        setRepositoryName(e.target.value);
    }

    const handleSubmit = async (e) => {
        e.preventDefault(); // 폼 새로고침 방지
    
        try {
            const response = await github(githubToken, githubName, repositoryName);
            if (response) {
                console.log("깃허브 페이지 결과");
                console.log("data : ", response);
                const message = response.message;
                console.log("message :", message);
                setMessage(response.message);

                localStorage.setItem('githubToken', githubToken);
                localStorage.setItem('githubName', githubName);
                localStorage.setItem('repositoryName', repositoryName);
                setShowPopup(true);
            }
        } catch (error) {
            console.error("Error occurred while calling the GitHub API:", error);
            if (error.response) {
                console.error("Server responded with:", error.response.data);
                console.error("Status code:", error.response.status);
            } else if (error.request) {
                console.error("No response received:", error.request);
            } else {
                console.error("Error setting up the request:", error.message);
            }
        }
    };

    const handlePopupClose=()=>{
        setShowPopup(false);
        Navigate('/mypage');
    }

    return (
        <div className="github-info-form">
            <h2>GitHub 정보 입력</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="githubToken">GitHub 토큰</label>
                    <input
                        type="text"
                        id="githubToken"
                        value={githubToken}
                        onChange={handleGithubToken}
                        placeholder="GitHub 토큰"
                    />
                </div>

                <div>
                    <label htmlFor="githubName">GitHub 사용자 이름:</label>
                    <input
                        type="text"
                        id="githubName"
                        value={githubName}
                        onChange={handleGithubName}
                        placeholder="GitHub 사용자 이름"
                    />
                </div>
                <div>
                    <label htmlFor="repositoryName">GitHub 레파지토리:</label>
                    <input
                        type="text"
                        id="repositoryName"
                        value={repositoryName}
                        onChange={handleRepositoryName}
                        placeholder="GitHub 레파지토리"
                    />
                </div>

                <button type="submit">정보 입력</button>

                {showPopup &&(
                    <div className="popup-overlay">
                        <div className="popup">
                            <h3>깃허브 연결 성공</h3>
                            <p>{message}</p>
                            <button onClick={handlePopupClose}>확인</button>
                        </div>
                    </div>
                )}

            </form>
        </div>
    );
};

export default Github_registration;



/*
<div>
                    <label htmlFor="githubToken">githubToken</label>
                    <input
                        type="text"
                        id="githubToken"
                        value={githubToken}
                        onChange={(e) => setGithubToken(e.target.value)}
                        placeholder="githubToken"
                    />
                </div>
*/