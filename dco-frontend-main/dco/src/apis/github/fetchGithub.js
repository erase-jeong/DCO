import axios from 'axios';

const SERVER_IP = process.env.REACT_APP_SERVER_IP;

export const github = async () => {
    const accessToken = localStorage.getItem('accessToken');

    try {
        const response = await axios.get(
            `${SERVER_IP}/api/member/github`,
            {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            }
        );
        console.log('Response Data:', response.data);
        return response;
    } catch (error) {
        console.error('Error occurred while calling the GitHub API:', error);
        throw error;
    }
};

export default github;
