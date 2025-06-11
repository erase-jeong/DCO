// groupData.js

const SERVER_IP = process.env.REACT_APP_SERVER_IP;

export const fetchGroup = async (groupId) => {
  const accessToken = localStorage.getItem('accessToken');
  console.log(accessToken);
  console.log(groupId);
  try {
    const response = await fetch(`${SERVER_IP}/api/group/member/${groupId}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`, // JWT 토큰 추가
      },
    });

    if (response.ok) {
      const data = await response.json();
      return data.data; // 그룹 데이터 반환
    } else {
      console.error('그룹 데이터 fetch 실패:', response.statusText);
      return null;
    }
  } catch (error) {
    console.error('그룹 데이터 fetch 에러:', error);
    return null; // 오류 발생 시 null 반환
  }
};
