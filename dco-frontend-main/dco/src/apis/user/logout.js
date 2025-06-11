import axios from 'axios';

const SERVER_IP = process.env.REACT_APP_SERVER_IP;

const logout = async () => {
  const accessToken = localStorage.getItem('accessToken');
  console.log('로그아웃 요청 전송 중...');
  localStorage.clear();

  try {
    const response = await axios.post(
      `${SERVER_IP}/api/member/logout`,
      {},
      {
        headers: {
          Authorization: `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
      }
    );

    // 응답 상태 확인
    if (response.status === 200) {
      console.log('로그아웃 성공:', response.data);
    } else {
      console.error('로그아웃 실패:', response.status, response.data);
    }

    //console.log('로그아웃 성공:', response.data);
    return response.data;
  } catch (error) {
    console.error(
      '로그아웃 실패:',
      error.response ? error.response.data : error.message
    );
    throw error;
  }
};

export default logout;
