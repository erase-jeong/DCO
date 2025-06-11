import axios from 'axios';

const SERVER_IP = process.env.REACT_APP_SERVER_IP;

export const github = async (githubToken, githubName, repositoryName) => {
  const accessToken = localStorage.getItem('accessToken');

  try {
    const response = await axios.post(
      `${SERVER_IP}/api/member/github`,
      { githubToken, githubName, repositoryName },
      {
        headers: {
          Authorization: `Bearer ${accessToken}`, // 헤더에 토큰 추가
        },
      }
    );
    console.log('Response Data:', response);
    return response;
  } catch (error) {
    console.error('Error occurred while calling the GitHub API:', error);
    if (error.response) {
      console.error('Server responded with:', error.response.data);
      console.error('Status code:', error.response.status);
    } else if (error.request) {
      console.error('No response received:', error.request);
    } else {
      console.error('Error setting up the request:', error.message);
    }
  }
};

export default github;
