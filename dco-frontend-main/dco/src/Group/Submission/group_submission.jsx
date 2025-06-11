import React from 'react';
import SubmissionTable from './group_submission_table';

const GroupNotification = () => {
  const notifications = [
    { id: 1, 
      title: '첫 번째 과제', 
      content: '첫 번째 과제 제출현황입니다.' ,
      all_student:100,
      submitter:30,
      non_summitter:70,
      score_100:20,
      score_90:1,
      score_80:0,
      score_70:9,
      score_0:70
    },
    { id: 2, title: '두 번째 과제', content: '두 번째 과제 제출현황입니다.' },
  ];


  return (
    <div>
      <h2>제출 현황</h2>
      <div>
        {notifications.map((notification) => (
          <div key={notification.id} style={{ border: '1px solid #ccc', padding: '10px', margin: '10px 0' }}>
            <h3>{notification.title}</h3>
            <p>{notification.content}</p>
            <p>전체 인원 : {notification.all_student}</p>
            <p>제출자 : {notification.submitter} / 미제출자 : {notification.non_summitter}</p>
            <SubmissionTable notifications={notifications}/>

          </div>
        ))}
      </div>
    </div>
  );
};

export default GroupNotification;

