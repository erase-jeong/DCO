import axios from 'axios';

const SERVER_IP = process.env.REACT_APP_SERVER_IP; // 환경 변수에서 서버 주소 가져오기

export const createGroup = async (groupName, year, semester) => {
  const accessToken = localStorage.getItem('accessToken');
  console.log(groupName, year, semester);
  const response = await axios.post(
    `${SERVER_IP}/api/group/professor`,
    { groupName, year, semester },
    {
      headers: {
        Authorization: `Bearer ${accessToken}`,
        'Content-Type': 'application/json',
      },
    }
  );

  console.log('Request Data:', { groupName, year, semester });
  console.log('Response Data (result):', response);
  console.log('Response Data (result.data):', response.data);

  return response.data;
};

export default createGroup;
