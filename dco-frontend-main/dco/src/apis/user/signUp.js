import axios from 'axios';

const SERVER_IP = process.env.REACT_APP_SERVER_IP;

// signUp.js 파일에서
const signUp = async (code, password, role, name) => {
  try {
    const result = await axios.post(`${SERVER_IP}/api/signup`, {
      code,
      password,
      role,
      name,
    });
    if (result && result.status) {
      // 응답이 성공적으로 왔을 경우
      console.log('Status:', result.status);
    } else {
      console.log('No status in the response');
    }

    console.log('Request Data:', { code, password, role, name });
    console.log('Response Data:', result.data);

    return result.data;
  } catch (error) {
    console.error('error출력', error);
    throw error.response ? error.response.data : error;
  }
};

export default signUp;
