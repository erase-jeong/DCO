// GroupAssignmentDetailCode.jsx
import React, { useState } from 'react';
import CodeMirror from '@uiw/react-codemirror';
import { javascript } from '@codemirror/lang-javascript';
import { python } from '@codemirror/lang-python';
import { java } from '@codemirror/lang-java';
import { cpp } from '@codemirror/lang-cpp';
import { dracula } from '@uiw/codemirror-theme-dracula';
import './group_assignment_detail_code.css';
import { submitAssignment } from '../../../apis/group/assignmentSubmit';

const GroupAssignmentDetailCode = ({ assignmentId }) => {
  const [code, setCode] = useState('// 여기에 코드를 작성하세요');
  const [language, setLanguage] = useState(cpp);
  const [theme, setTheme] = useState(dracula);
  const [output, setOutput] = useState('');

  const handleRun = () => {
    console.log('실행된 코드:', code);
    // ToDo
    setOutput('Hello, World! (실행 결과)');
  };

  const handleSubmit = async () => {
    // console.log('이거 확인해 (assignmentId):', assignmentId);
    const payload = {
      problemId: +assignmentId,
      code,
      language: language.language.name.toUpperCase(),
    };
    console.log('제출된 코드:', code);
    console.log('payload:', payload);
    try {
      const response = await submitAssignment(payload);

      if (response && response.message) {
        alert(response.message);
      } else {
        alert('과제 제출이 완료되었습니다.');
      }
    } catch (error) {
      console.error('과제 제출 중 오류 발생:', error);
      alert(error.message || '과제 제출 중 오류가 발생했습니다.');
    }
    setOutput('(제출 버튼을 클릭했습니다.)');
  };

  const handleLanguageChange = (event) => {
    const selectedLanguage = event.target.value;
    switch (selectedLanguage) {
      case 'cpp':
        setLanguage(cpp);
        break;
      case 'java':
        setLanguage(java);
        break;
      case 'javascript':
        setLanguage(javascript);
        break;
      case 'python':
        setLanguage(python);
        break;
      default:
        setLanguage(cpp);
    }
  };

  return (
    <div className="code-editor-container">
      <div className="editor-options">
        <label>
          언어 선택:
          <select className="select-language" onChange={handleLanguageChange}>
            <option value="cpp">C/C++</option>
            <option value="java">Java</option>
            <option value="javascript">JavaScript</option>
            <option value="python">Python</option>
          </select>
        </label>
      </div>
      <CodeMirror
        className="CodeMirror"
        value={code}
        height="300px"
        extensions={language}
        theme={theme}
        onChange={(value) => {
          setCode(value);
        }}
      />
      <div className="output-console">{output || '코드 실행 결과'}</div>
      <div className="editor-actions">
        <button className="run-button" onClick={handleRun}>
          코드 실행
        </button>
        <button className="submit-button" onClick={handleSubmit}>
          제출
        </button>
      </div>
    </div>
  );
};

export default GroupAssignmentDetailCode;
