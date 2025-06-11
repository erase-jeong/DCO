// ../../apis/assignment.js

const SERVER_IP = process.env.REACT_APP_SERVER_IP;

export const fetchAssignmentDetail = async (problemId) => {
  const accessToken = localStorage.getItem('accessToken');
  console.log(accessToken);
  console.log(problemId);
  try {
    const response = await fetch(
      `${SERVER_IP}/api/problem/member/detail?problemId=${problemId}`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${accessToken}`, // JWT 토큰 추가
        },
      }
    );

    if (response.ok) {
      const data = await response.json();
      return data.data; // 과제 목록 반환
    }
  } catch (error) {
    console.error('assignment fetch 에러:', error);
    return null; // 오류 발생 시 null 반환
  }
};
