"use client";

import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import PropTypes from "prop-types";
import { fetchGroupGrading } from "../../apis/group/assignmentGranding";
// import { fetchAssignments } from "../../apis/group/assignment";

export default function GroupSubmissionStatus({ memberId = 3 }) {
  const { groupId } = useParams();
  const [submissions, setSubmissions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);
      try {
        const result = await fetchGroupGrading(groupId, memberId);
        if (result && result.data) {
          setSubmissions(result.data);
        } else {
          console.log("groupId", groupId);
          setError("서버로부터 데이터를 받지 못했습니다");
        }
      } catch (err) {
        setError("제출 데이터를 가져오는데 실패했습니다");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [groupId, memberId]);

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleString("ko-KR", { timeZone: "Asia/Seoul" });
  };

  if (error) {
    return (
      <div className="w-full max-w-4xl mx-auto p-6">
        <p className="text-red-500" role="alert">
          {error}
        </p>
      </div>
    );
  }

  return (
    <div className="w-full max-w-4xl mx-auto">
      <div className="mb-4">
        <h2 className="text-2xl font-bold">그룹 제출 현황</h2>
      </div>
      <div className="overflow-x-auto">
        <table className="w-full border-collapse">
          <thead>
            <tr className="bg-gray-100">
              <th className="border p-2">이름</th>
              <th className="border p-2">문제</th>
              <th className="border p-2">점수</th>
              <th className="border p-2">제출 시간</th>
              <th className="border p-2">언어</th>
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr>
                <td colSpan={5} className="text-center p-4">
                  <div className="animate-pulse bg-gray-200 h-6 w-3/4 mx-auto"></div>
                </td>
              </tr>
            ) : submissions.length === 0 ? (
              <tr>
                <td colSpan={5} className="text-center p-4">
                  제출된 내역이 없습니다
                </td>
              </tr>
            ) : (
              submissions.map((submission) => (
                <tr
                  key={`${submission.memberId}-${submission.problemId}`}
                  className="hover:bg-gray-50"
                >
                  <td className="border p-2">{submission.name}</td>
                  <td className="border p-2">{submission.title}</td>
                  <td className="border p-2">{submission.score}</td>
                  <td className="border p-2">
                    {formatDate(submission.submitTime)}
                  </td>
                  <td className="border p-2">{submission.language}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

GroupSubmissionStatus.propTypes = {
  groupId: PropTypes.number,
  memberId: PropTypes.number,
};
