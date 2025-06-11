// ../../apis/assignment_grading.js

const SERVER_IP = process.env.REACT_APP_SERVER_IP;

export const fetchGroupGrading = async (groupId, memberId) => {
  const accessToken = localStorage.getItem("accessToken");

  try {
    const response = await fetch(
      `${SERVER_IP}/api/submission/professor/${groupId}/${memberId}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`, // JWT 토큰 추가
        },
      }
    );

    if (response.ok) {
      const data = await response.json();
      return data; // API 응답 데이터 반환
    } else {
      throw new Error("Server responded with an error");
    }
  } catch (error) {
    console.error("Group grading fetch 에러:", error);
    return null; // 오류 발생 시 null 반환
  }
};
