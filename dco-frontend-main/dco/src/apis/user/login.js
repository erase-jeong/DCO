import axios from 'axios';

const SERVER_IP = process.env.REACT_APP_SERVER_IP;

export const login = async (code, password) => {
  const response = await axios.post(`${SERVER_IP}/api/login`, {
    code,
    password,
  });

  console.log('Request Data:', { code, password });
  console.log('Response Data (result):', response);
  console.log('Response Data (result.data):', response.data);

  return response.data;
};

export default login;
