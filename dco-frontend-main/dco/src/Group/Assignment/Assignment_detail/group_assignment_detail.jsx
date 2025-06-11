import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Viewer } from '@toast-ui/react-editor';
import GroupAssignmentDetailCode from './group_assignment_detail_code';
import SplitPane from 'react-split-pane';
import '@toast-ui/editor/dist/toastui-editor-viewer.css';
import './group_assignment_detail.css';
import { fetchAssignmentDetail } from '../../../apis/group/assignmentDetail'; // API 호출 함수 임포트

import Loading from '../../../Components/loading.jsx';

const AssignmentDetail = () => {
  const { groupId, assignmentId } = useParams();
  console.log('groupId:', groupId);
  console.log('assignmentId:', assignmentId);
  const [showCodeEditor, setShowCodeEditor] = useState(false);

  const [assignment, setAssignment] = useState(null); // 초기값을 null로 설정
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    console.log('assignmentId: ', assignmentId);

    const getAssignmentDetail = async () => {
      try {
        const data = await fetchAssignmentDetail(assignmentId); // API 호출

        if (data) {
          setAssignment(data);
          console.log('받아온 데이터 확인: ', data); // 데이터 확인
        } else {
          console.error('받아온 데이터가 없습니다.');
        }
      } catch (error) {
        console.error('과제 상세 정보를 가져오는 중 오류 발생:', error);
      } finally {
        setLoading(false); // 로딩 완료
      }
    };

    getAssignmentDetail();
  }, [assignmentId]);

  const handleShowCodeEditor = () => {
    setShowCodeEditor(true);
  };

  const calculateDaysLeft = (dueDate) => {
    const now = new Date();
    const due = new Date(dueDate);

    // 날짜 부분만 비교하기 위해: 시간 정보 제거
    const startOfToday = new Date(
      now.getFullYear(),
      now.getMonth(),
      now.getDate()
    );
    const startOfDueDate = new Date(
      due.getFullYear(),
      due.getMonth(),
      due.getDate()
    );

    const timeDiff = startOfDueDate - startOfToday;
    const daysLeft = Math.floor(timeDiff / (1000 * 60 * 60 * 24));

    let dDayLabel = `D${daysLeft >= 0 ? '-' : '+'}${Math.abs(daysLeft)}`;
    let labelClass = '';

    if (daysLeft >= 3) labelClass = 'd-day-gray';
    else if (daysLeft === 2) labelClass = 'd-day-yellow';
    else if (daysLeft === 1) labelClass = 'd-day-orange';
    else if (daysLeft === 0) labelClass = 'd-day-red';
    else labelClass = 'd-day-past';

    return { dDayLabel, labelClass };
  };

  if (loading) {
    return <Loading />;
  }

  if (!assignment) {
    return <div>과제를 찾을 수 없습니다.</div>;
  }

  const { dDayLabel, labelClass } = calculateDaysLeft(assignment.endTime);

  return (
    <div className="assignment-detail-container">
      {/* <SplitPane
        split="vertical"
        minSize={500}
        maxSize={-500}
        defaultSize="50%"
      > */}
      <div className="assignment-container">
        <h2 className="assignment-title">{assignment.title}</h2>
        <hr />
        <div className="assignment-row">
          <div className="assignment-label">
            <span className="assignment-label-name">과제 열람일</span>
            <span className="assignment-label-value">
              {new Date(assignment.startTime).toLocaleString()}
            </span>
          </div>
          <div className="assignment-label">
            <span className="assignment-label-name">점수</span>
            <span className="assignment-label-value">
              {assignment.score}/{assignment.maxScore}
            </span>
          </div>
        </div>
        <div className="assignment-row">
          <div className="assignment-label">
            <span className="assignment-label-name">과제 마감일</span>
            <span className="assignment-label-value">
              {new Date(assignment.endTime).toLocaleString()}
              <span className={`d-day-box ${labelClass}`}>{dDayLabel}</span>
            </span>
          </div>
          <div className="assignment-label">
            <span className="assignment-label-value">
              <span
                className={`assignment-list-status ${
                  assignment.status === '미제출'
                    ? 'status-pending'
                    : 'status-submitted'
                }`}
              >
                {assignment.status}
              </span>
            </span>
          </div>
        </div>
        <hr />
        <div className="assignment-content">
          <Viewer initialValue={assignment.content} />
        </div>
      </div>
      <div className="code-editor-section">
        <GroupAssignmentDetailCode assignmentId={assignmentId} />
      </div>
      {/* </SplitPane> */}
    </div>
  );
};

export default AssignmentDetail;
