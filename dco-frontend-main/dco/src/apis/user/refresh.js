import axios from 'axios';

const SERVER_IP = process.env.REACT_APP_SERVER_IP;

export const getNewRefreshToken = async () => {
  console.log('refresh 토큰 갱신 완료');
  const accessToken = localStorage.getItem('accessToken');
  const refreshToken = localStorage.getItem('refreshToken');

  const result = await axios.post(
    `${SERVER_IP}/api/member/refresh`,
    {
      refreshToken,
    },
    {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    }
  );
  console.log('refresh API : ', result.data);
  console.log('refresh API(AT) : ', result.data.data.accessToken);

  return result.data.data;
};
