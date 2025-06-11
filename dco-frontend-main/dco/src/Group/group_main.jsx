// Group.jsx
import React, { useEffect, useState } from 'react';
import { NavLink, Route, Routes, Navigate, useParams } from 'react-router-dom';
import GroupNotification from './Notification/group_notification';
import GroupNotificationDetail from './Notification/Notification_detail/group_notification_detail';
import GroupNotificationWrite from './Notification/Notification_write/group_notification_write';
import GroupAssignment from './Assignment/group_assignment';
import GroupSubmission from './Submission/group_submission';
import GroupGranding from './Granding/group_granding';
import GroupQuestion from './Question/group_question';
import AssignmentDetail from './Assignment/Assignment_detail/group_assignment_detail';
import GroupAssignmentWrite from './Assignment/Assignment_write/group_assignment_write';
import ErrorPage from './Assignment/Assignment_error/error_page';
import GrandingDetail from './Granding/group_granding_detail';

import group_image from '../assets/images/Python_icon.png';
import './group_main.css';
import { fetchGroup } from '../apis/group/groupData'; // 그룹 API 함수 임포트
import Loading from '../Components/loading.jsx';

const Group = () => {
  const { groupId } = useParams();
  const [groupData, setGroupData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // semester 값을 매핑하는 객체
  const semesterDisplay = {
    FIRST: '1학기',
    SECOND: '2학기',
    SUMMER: '하계계절학기',
    WINTER: '동계계절학기',
  };

  useEffect(() => {
    const getGroupData = async () => {
      setLoading(true);
      setError(null);
      const data = await fetchGroup(groupId);
      if (data) {
        setGroupData(data);
      } else {
        setError('그룹을 볼 수 없습니다.');
      }
      setLoading(false);
    };

    getGroupData();
  }, [groupId]);

  if (loading) {
    return <Loading />;
  }

  if (error) {
    return (
      <div className="group-container">
        <div className="error">{error}</div>
      </div>
    );
  }

  return (
    <div className="group-container">
      {/* [1] 그룹 이미지 */}
      <div className="group-image-container">
        <img src={group_image} alt="Logo" className="group-image" />
      </div>
      {/* [2] 그룹 이름, 년도/학기, 담당 교수 */}
      <div className="group-header">
        <div className="group-info">
          <h1 className="group-name">{groupData.groupName}</h1>
          <span className="group-year-semester">
            | {groupData.year}년{' '}
            {semesterDisplay[groupData.semester] || '학기 정보 없음'}
          </span>
        </div>
        <div className="group-leader">{groupData.leaderName}</div>
      </div>
      {/* [3] 탭 네비게이션 */}
      <div className="group-tabs">
        <NavLink
          to={`/group/${groupId}/notification`}
          className={({ isActive }) =>
            'group-tab' + (isActive ? ' active' : '')
          }
        >
          공지
        </NavLink>
        <NavLink
          to={`/group/${groupId}/assignment`}
          className={({ isActive }) =>
            'group-tab' + (isActive ? ' active' : '')
          }
        >
          과제
        </NavLink>
        <NavLink
          to={`/group/${groupId}/submission`}
          className={({ isActive }) =>
            'group-tab' + (isActive ? ' active' : '')
          }
        >
          제출 현황
        </NavLink>
        <NavLink
          to={`/group/${groupId}/granding`}
          className={({ isActive }) =>
            'group-tab' + (isActive ? ' active' : '')
          }
        >
          채점 현황
        </NavLink>
        <NavLink
          to={`/group/${groupId}/question`}
          className={({ isActive }) =>
            'group-tab' + (isActive ? ' active' : '')
          }
        >
          질문 게시판
        </NavLink>
      </div>
      <div>
        {/* 라우트 설정 */}
        <Routes>
          <Route path="/" element={<Navigate to="notification" />} />
          <Route path="notification" element={<GroupNotification />} />
          <Route
            path="notification/:notificationId"
            element={<GroupNotificationDetail />}
          />
          <Route
            path="notification/write"
            element={<GroupNotificationWrite />}
          />
          <Route path="assignment" element={<GroupAssignment />} />
          <Route path="assignment/write" element={<GroupAssignmentWrite />} />
          <Route
            path="assignment/:assignmentId"
            element={<AssignmentDetail />}
          />
          <Route path="submission" element={<GroupSubmission />} />
          <Route path="granding" element={<GroupGranding />} />
          <Route path="granding/:grandingId" element={<GrandingDetail />} />
          <Route path="question" element={<GroupQuestion />} />
          {/* 열리지 않은 과제일 때, 이동 페이지 */}
          <Route path="assignment/error" element={<ErrorPage />} />{' '}
        </Routes>
      </div>
    </div>
  );
};

export default Group;
