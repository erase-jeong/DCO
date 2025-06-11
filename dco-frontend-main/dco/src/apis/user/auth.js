import axios from 'axios';
import { getNewRefreshToken } from './refresh';

axios.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const data = await getNewRefreshToken();
        localStorage.setItem('accessToken', data.accessToken);
        console.log('accessToken:', data.accessToken);
        axios.defaults.headers.common[
          'Authorization'
        ] = `Bearer ${data.accessToken}`;

        return axios(originalRequest);
      } catch (refreshError) {
        console.error('토큰 갱신 실패:', refreshError);
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

//[토큰 유효성 검사] 임의로 토큰 만료 => 토큰 갱신 실패시켜서 /refresh 사용하려면 이거 사용
export const checkTokenValidity = () => {
  return false;
};

//[토큰 유효성 검사] = 정상작동하는 코드
/*
export const checkTokenValidity = () => {
  const accessToken = localStorage.getItem('accessToken');
  console.log('Access Token:', accessToken); // 토큰 값 로그 출력
  if (!accessToken) {
    return false;
  }

  const tokenPayload = JSON.parse(atob(accessToken.split('.')[1]));
  const expiryTime = tokenPayload.exp * 1000;
  const currentTime = Date.now();

  return currentTime < expiryTime;
};
*/
