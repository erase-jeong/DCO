// GroupAssignment.jsx
import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import ListItem from '../../assets/list_item';
import { fetchAssignments } from '../../apis/group/assignment';
import './group_assignment.css';
import Loading from '../../Components/loading.jsx';

const GroupAssignment = () => {
  const { groupId } = useParams();
  const [assignments, setAssignments] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    console.log('groupId: ', groupId);

    const getAssignments = async () => {
      const data = await fetchAssignments(groupId);

      if (data) {
        setAssignments(data);
        console.log('Fetched assignments: ', data);
      }

      setLoading(false);
    };

    getAssignments();
  }, [groupId]);

  // 과제가 잠겨 있는지 확인하는 함수
  const isAssignmentLocked = (startTime) => {
    const now = new Date();
    const start = new Date(startTime);
    return start > now;
  };

  if (loading) {
    return <Loading />;
  }

  return (
    <div className="assignment-list-container">
      <div className="assignment-list-actions">
        <Link to={`/group/${groupId}/assignment/write`}>
          <button className="assignment-write-button">과제 생성</button>
        </Link>
      </div>
      <div className="assignment-list">
        <div className="assignment-list-header">
          <span className="assignment-list-title">과제 제목</span>
          <span className="assignment-list-start-date">과제 시작일</span>
          <span className="assignment-list-end-date">과제 마감일</span>
          <span className="assignment-list-score">점수</span>
          <span className="assignment-list-status">상태</span>
        </div>
        {assignments.map((assignment) => {
          const locked = isAssignmentLocked(assignment.startTime);
          const link = locked
            ? `/group/${groupId}/assignment/error` // 과제가 잠겨있을 때 에러 페이지로 링크 변경
            : `/group/${groupId}/assignment/${assignment.problemId}`; // 잠겨있지 않을 때 정상 과제 상세 페이지로 링크

          return (
            <ListItem
              key={assignment.problemId}
              link={link}
              title={assignment.title}
              columns={[
                {
                  content: new Date(assignment.startTime).toLocaleString(),
                  className: 'assignment-list-start-date',
                },
                {
                  content: new Date(assignment.endTime).toLocaleString(),
                  className: 'assignment-list-end-date',
                },
                {
                  content: assignment.score || '-',
                  className: 'assignment-list-score',
                },
                // 상태 표시를 원하시면 아래 주석을 해제하세요
                {
                  content: assignment.status || '상태 없음',
                  className: `assignment-list-status ${
                    assignment.status === '미제출'
                      ? 'status-pending'
                      : 'status-submitted'
                  }`,
                },
              ]}
            />
          );
        })}
      </div>
    </div>
  );
};

export default GroupAssignment;
