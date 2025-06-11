// src/components/GroupList.jsx
import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './grouplist_main.css';
import GroupItem from './group_item';
import { fetchGroup } from '../apis/group_list/group_list';
import Loading from '../Components/loading';

const GroupList = () => {
  const [groups, setGroups] = useState([]); // 그룹 데이터를 저장할 상태
  const [loading, setLoading] = useState(true); // 로딩 상태
  const [error, setError] = useState(null); // 오류 상태
  const navigate = useNavigate(); // useNavigate 훅 초기화

  useEffect(() => {
    const getGroups = async () => {
      try {
        const data = await fetchGroup();
        console.log('data:', data);
        if (data && Array.isArray(data)) {
          setGroups(data);
          console.log('Groups <- data 완료');
        }
      } catch (err) {
        console.error('그룹 리스트 데이터 fetch 에러:', err);
        setError('그룹 데이터를 불러오는 데 실패했습니다.');
      } finally {
        setLoading(false);
      }
    };

    getGroups();
  }, []);

  const handleGroupClick = (groupId) => {
    console.log(`그룹 ID: ${groupId} 클릭됨`);

    // if (someCondition) {
    //   navigate(`/another-path/${groupId}`);
    // } else {
    //   navigate(`/group/${groupId}/notification`);
    // }

    // 일반 경로로 갈 경우, 바로 /notification으로 이동 되므로 뒤로가기가 안되는 문제
    // => 애초에 notification으로 이동하도록 변경
    navigate(`/group/${groupId}/notification`);
  };

  if (loading) {
    return <Loading />;
  }

  if (error) {
    return <div className="error">{error}</div>;
  }

  return (
    <div className="group-list-container">
      <div className="group-create-wrapper">
        <Link to="/grouplist/creation">
          <button className="group-create-button">그룹 생성하기</button>
        </Link>
      </div>

      {groups.length === 0 ? (
        <div className="no-groups">현재 생성된 그룹이 없습니다.</div>
      ) : (
        <div className="groups-grid">
          {console.log('groups', groups)}
          {groups.map((group) => (
            <div
              key={group.id} // key를 div에 할당
              onClick={() => handleGroupClick(group.groupId)}
              style={{ cursor: 'pointer', textDecoration: 'none' }}
              className="group-item-wrapper"
            >
              <GroupItem
                title={group.groupName}
                professor={group.leaderName}
                semester={group.semester}
                // imageUrl={group.imageUrl} // 서버에서 이미지 URL을 제공하는 경우 추가
              />
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default GroupList;
