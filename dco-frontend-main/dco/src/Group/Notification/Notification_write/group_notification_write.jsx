import React, { useRef, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Editor } from '@toast-ui/react-editor';
import '@toast-ui/editor/dist/toastui-editor.css';
// import './group_assignment_write.css';

const GroupNotificationWrite = () => {
  const editorRef = useRef();
  const { groupId } = useParams();

  // 열람 가능 시간 설정을 위한 함수
  //   const getDefaultViewableTime = () => {
  //     const now = new Date();
  //     const localISOString = new Date(
  //       now.getTime() - now.getTimezoneOffset() * 60000
  //     ).toISOString();
  //     return localISOString.substring(0, localISOString.length - 8);
  //   };

  // 데드라인 설정을 위한 함수
  // ([현재 날짜의 +6]일, 오후 11시 59분을 기본으로 설정)
  //   const getDefaultDeadline = () => {
  //     const now = new Date();
  //     const deadline = new Date(now.setDate(now.getDate() + 6));
  //     deadline.setHours(23, 59, 0, 0);
  //     const localISOString = new Date(
  //       deadline.getTime() - deadline.getTimezoneOffset() * 60000
  //     ).toISOString();
  //     return localISOString.substring(0, localISOString.length - 8);
  //   };

  const [title, setTitle] = useState('');
  //   const [deadline, setDeadline] = useState(getDefaultDeadline());
  //   const [pythonExcuteTimeLimit, setPythonExcuteTimeLimit] = useState('');
  //   const [cppExcuteTimeLimit, setCppExcuteTimeLimit] = useState('');
  //   const [javaExcuteTimeLimit, setJavaExcuteTimeLimit] = useState('');
  //   const [submissionLimit, setSubmissionLimit] = useState('');
  //   const [viewableTime, setViewableTime] = useState(getDefaultViewableTime());

  const handleSubmit = async () => {
    const editorInstance = editorRef.current.getInstance(); // 에디터 인스턴스에 접근 후 마크다운 형식으로 입력된 텍스트 저장
    const content = editorInstance.getMarkdown(); // 위에서 저장한 내용을 서버에 전송할 content 변수로 저장

    const payload = {
      title,
      content,
      //   startTime: viewableTime,
      //   endTime: deadline,
      //   pythonExcuteTimeLimit: parseInt(pythonExcuteTimeLimit) || 0,
      //   cppExcuteTimeLimit: parseInt(cppExcuteTimeLimit) || 0,
      //   javaExcuteTimeLimit: parseInt(javaExcuteTimeLimit) || 0,
      //   submitLimit: parseInt(submissionLimit) || 0,
      groupId: parseInt(groupId),
    };

    // 작성 공지 서버 전송
    //     try {
    //       const response = await fetch('/api/problem', {
    //         method: 'POST',
    //         headers: {
    //           'Content-Type': 'application/json',
    //         },
    //         body: JSON.stringify(payload),
    //       });

    //       if (response.ok) {
    //         alert('공지가 성공적으로 생성되었습니다.');
    //       } else {
    //         console.log(groupId);
    //         alert('공지 생성에 실패했습니다.');
    //       }
    //     } catch (error) {
    //       console.error('Error:', error);
    //       alert('공지 생성 중 오류가 발생했습니다.');
    //     }
    //   };
  };

  return (
    <div className="assignment-write-container">
      <h2 className="assignment-write-title">공지 작성</h2>
      <input
        type="text"
        placeholder="공지 제목을 입력하세요"
        className="assignment-write-input"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
      />
      <Editor
        ref={editorRef}
        initialValue=" "
        height="400px"
        initialEditType="markdown"
        hideModeSwitch
        useCommandShortcut={true}
      />
      <button className="assignment-write-submit" onClick={handleSubmit}>
        공지 생성
      </button>
    </div>
  );
};

export default GroupNotificationWrite;
