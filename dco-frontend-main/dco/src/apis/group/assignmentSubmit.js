// assignmentSubmit.js

const SERVER_IP = process.env.REACT_APP_SERVER_IP;

export const submitAssignment = async (payload) => {
  const accessToken = localStorage.getItem('accessToken');
  console.log(accessToken);
  console.log(payload);
  try {
    const response = await fetch(`${SERVER_IP}/api/submission/member`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`,
      },
      body: JSON.stringify(payload),
    });

    const data = await response.json();

    if (response.ok) {
      return data;
    } else {
      throw new Error(data.message || '제출 실패');
    }
  } catch (error) {
    console.error('API 호출 에러:', error);
    throw error;
  }
};
