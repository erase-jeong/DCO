import React, { useRef, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Editor } from '@toast-ui/react-editor';
import '@toast-ui/editor/dist/toastui-editor.css';
import './group_assignment_write.css';
import { createAssignment } from '../../../apis/group/assignmentWrite';

import { toast } from 'react-toastify';

const GroupAssignmentWrite = () => {
  const editorRef = useRef();
  const { groupId } = useParams();
  const navigate = useNavigate();

  // 열람 가능 시간 설정을 위한 함수
  const getDefaultViewableTime = () => {
    const now = new Date();
    const localISOString = new Date(
      now.getTime() - now.getTimezoneOffset() * 60000
    ).toISOString();
    return localISOString.substring(0, localISOString.length - 8);
  };

  // 데드라인 설정을 위한 함수
  const getDefaultDeadline = () => {
    const now = new Date();
    const deadline = new Date(now.setDate(now.getDate() + 6));
    deadline.setHours(23, 59, 0, 0);
    const localISOString = new Date(
      deadline.getTime() - deadline.getTimezoneOffset() * 60000
    ).toISOString();
    return localISOString.substring(0, localISOString.length - 8);
  };

  const [title, setTitle] = useState('');
  const [deadline, setDeadline] = useState(getDefaultDeadline());
  const [pythonExcuteTimeLimit, setPythonExcuteTimeLimit] = useState('');
  const [cppExcuteTimeLimit, setCppExcuteTimeLimit] = useState('');
  const [javaExcuteTimeLimit, setJavaExcuteTimeLimit] = useState('');
  const [submissionLimit, setSubmissionLimit] = useState('');
  const [viewableTime, setViewableTime] = useState(getDefaultViewableTime());

  // 테스트케이스 상태 관리
  const [testcaseList, setTestcaseList] = useState([{ input: '', output: '' }]);

  const handleAddTestcase = () => {
    setTestcaseList([...testcaseList, { input: '', output: '' }]);
  };

  const handleRemoveTestcase = (index) => {
    setTestcaseList(testcaseList.filter((_, i) => i !== index));
  };

  const handleTestcaseInputChange = (e, index) => {
    const newTestcaseList = [...testcaseList];
    newTestcaseList[index].input = e.target.value;
    setTestcaseList(newTestcaseList);
  };

  const handleTestcaseOutputChange = (e, index) => {
    const newTestcaseList = [...testcaseList];
    newTestcaseList[index].output = e.target.value;
    setTestcaseList(newTestcaseList);
  };

  const handleSubmit = async () => {
    const editorInstance = editorRef.current.getInstance();
    const content = editorInstance.getMarkdown();

    const payload = {
      title,
      content,
      startTime: viewableTime,
      endTime: deadline,
      pythonExecuteTimeLimit: parseInt(pythonExcuteTimeLimit) || 0,
      cppExecuteTimeLimit: parseInt(cppExcuteTimeLimit) || 0,
      javaExecuteTimeLimit: parseInt(javaExcuteTimeLimit) || 0,
      submitLimit: parseInt(submissionLimit) || 0,
      runtimeLimit: 10, // 일단 있어야 생성됨. 서버에서 처리 완료되면 지울 것
      groupId: parseInt(groupId),
      testcaseList: testcaseList.map((testcase, index) => ({
        number: index,
        input: testcase.input,
        output: testcase.output,
      })),
    };

    try {
      const response = await createAssignment(payload); // API 호출

      console.log('[과제 생성] response', response);
      // 성공 시 메시지 표시
      if (response) {
        toast.success(`${response.message}`);
        // console.log(response.message);
      }

      // 페이지 이동
      navigate(-1);
    } catch (error) {
      // console.error('과제 생성 중 오류 발생: ', error);
      toast.error(`${error.message}`);
    }
  };

  return (
    <div className="assignment-write-container">
      <h2 className="assignment-write-title">과제 작성</h2>
      <input
        type="text"
        placeholder="과제 제목을 입력하세요"
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
      <div className="assignment-write-grid">
        <input
          type="number"
          placeholder="Python 실행 시간 제한 (초)"
          className="assignment-write-input"
          value={pythonExcuteTimeLimit}
          onChange={(e) => setPythonExcuteTimeLimit(e.target.value)}
        />
        <input
          type="number"
          placeholder="C++ 실행 시간 제한 (초)"
          className="assignment-write-input"
          value={cppExcuteTimeLimit}
          onChange={(e) => setCppExcuteTimeLimit(e.target.value)}
        />
        <input
          type="number"
          placeholder="Java 실행 시간 제한 (초)"
          className="assignment-write-input"
          value={javaExcuteTimeLimit}
          onChange={(e) => setJavaExcuteTimeLimit(e.target.value)}
        />
        <input
          type="number"
          placeholder="제출 횟수 제한 (선택)"
          className="assignment-write-input"
          value={submissionLimit}
          onChange={(e) => setSubmissionLimit(e.target.value)}
        />
        <input
          type="datetime-local"
          placeholder="열람 가능 시간"
          className="assignment-write-input"
          value={viewableTime}
          onChange={(e) => setViewableTime(e.target.value)}
        />
        <input
          type="datetime-local"
          placeholder="마감기한"
          className="assignment-write-input"
          value={deadline}
          onChange={(e) => setDeadline(e.target.value)}
        />
      </div>
      <div className="testcase-list">
        <h3 className="testcase-title">테스트케이스</h3>
        {testcaseList.map((testcase, index) => (
          <div key={index} className="testcase-item">
            <textarea
              placeholder="입력"
              className="testcase-input"
              value={testcase.input}
              onChange={(e) => handleTestcaseInputChange(e, index)}
            />
            <textarea
              placeholder="출력"
              className="testcase-output"
              value={testcase.output}
              onChange={(e) => handleTestcaseOutputChange(e, index)}
            />
            <button
              className="testcase-remove-button"
              onClick={() => handleRemoveTestcase(index)}
            >
              -
            </button>
          </div>
        ))}
        <button className="testcase-add-button" onClick={handleAddTestcase}>
          +
        </button>
      </div>
      <button className="assignment-write-submit" onClick={handleSubmit}>
        과제 생성
      </button>
    </div>
  );
};

export default GroupAssignmentWrite;
