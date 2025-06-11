// assignmentWrite.js

const SERVER_IP = process.env.REACT_APP_SERVER_IP;

export const createAssignment = async (payload) => {
  const accessToken = localStorage.getItem('accessToken');
  console.log(accessToken);
  console.log(payload);
  try {
    const response = await fetch(`${SERVER_IP}/api/problem/professor`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`, // JWT 토큰 추가
      },
      body: JSON.stringify(payload),
    });

    const data = await response.json(); // 응답 데이터를 파싱합니다.

    if (response.ok) {
      return data; // 성공 시 데이터를 반환합니다.
    } else {
      throw new Error(data.message || '과제 생성에 실패하였습니다.'); // 에러를 던집니다.
    }
  } catch (error) {
    console.error('API 호출 에러:', error);
    throw error; // 에러를 상위로 전달합니다.
  }
};
