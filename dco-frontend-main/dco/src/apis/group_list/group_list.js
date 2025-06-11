const SERVER_IP = process.env.REACT_APP_SERVER_IP;

export const fetchGroup = async () => {
  const accessToken = localStorage.getItem('accessToken');
  console.log(accessToken);
  //   console.log(groupId);
  try {
    const response = await fetch(`${SERVER_IP}/api/group/member/list`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`,
      },
    });

    if (response.ok) {
      const data = await response.json();
      console.log('group list:', data);
      return data.data;
    } else {
      console.error('그룹 리스트 데이터 fetch 실패:', response.statusText);
      return null;
    }
  } catch (error) {
    console.error('그룹 리스트 데이터 fetch 에러:', error);
    return null; // 오류 발생 시 null 반환
  }
};
