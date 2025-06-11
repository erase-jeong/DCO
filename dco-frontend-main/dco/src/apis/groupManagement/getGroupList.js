import axios from 'axios';
import getGroupList from './getGroupList';


const SERVER_IP = process.env.REACT_APP_SERVER_IP; // 환경 변수에서 서버 주소 가져오기

export const getGroupList = async (groupName, year, semester) => {
  const accessToken = localStorage.getItem('accessToken');
  console.log(groupName, year, semester);
  const response = await axios.(
    `${SERVER_IP}/api/group/professor/list`,
    /*{ groupName, year, semester },*/
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

export default getGroupList;
