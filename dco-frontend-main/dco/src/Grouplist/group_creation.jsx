import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './group_creation.css';
import createGroup from '../apis/createGroup';
import { useAuth } from '../AuthContext';
import { jwtDecode } from 'jwt-decode';
import { toast } from 'react-toastify';

const Group_creation = () => {
  const [showPopup, setShowPopup] = useState(false); //팝업 상태
  const [groupName, setGroupName] = useState('');
  const [year, setYear] = useState('');
  const [semester, setSemester] = useState('');

  const navigate = useNavigate();

  const userRole = localStorage.getItem('userRole');

  const handleGroupNameChange = (e) => {
    setGroupName(e.target.value);
  };

  const handleYearChange = (e) => {
    setYear(e.target.value);
  };

  const handleSemesterChange = (e) => {
    setSemester(e.target.value);
  };

  const handleGroupCreation = async (e) => {
    e.preventDefault();

    try {
      const response = await createGroup(groupName, year, semester);
      console.log('Group created:', response);

      toast.success(
        `${response.data.groupName} 그룹이 성공적으로 생성되었습니다!`
      );
      navigate(`/group/${response.data.groupId}`);
    } catch (error) {
      console.error('Error creating group:', error);

      toast.error('그룹 생성 중 오류가 발생했습니다.');
    }
  };

  // const handleYearChange=(e)=>{
  //     setYear(e.target.value);
  // }

  // const handleSemesterChange=(e)=>{
  //     setSemester(e.target.value);
  // }

  // const handleGroupCreation= async (e)=>{

  //     e.preventDefault();

  //     try {
  //         const response = await createGroup(groupName, year, semester);

  //         console.log('Group created:', response);
  //         setShowPopup(true);

  //         // Navigate to group list or any other page
  //         //navigate(`/grouplist?groupName=${groupName}&year=${year}&semester=${semester}`);
  //     } catch (error) {
  //         console.error('Error creating group:', error);
  //         // Handle error (e.g., show an error message)
  //     }

  /*
    //};
    /*
        const formData=new FormData();
        formData.append("groupName");
        formData.append("Year");
        formData.append("Semester");
        formData.append("groupContent");
        */

  //이미지는 일단 나중에

  // const handlePopupClose=()=>{
  //     setShowPopup(false);
  //     navigate('/grouplist');
  // }

  return (
    <div>
      <div className="group-creation-container">
        <h2>그룹 생성하기</h2>
        <form onSubmit={handleGroupCreation}>
          <div className="form-group">
            <label htmlFor="groupName">그룹명</label>
            <input
              id="groupName"
              value={groupName}
              onChange={handleGroupNameChange}
              placeholder="그룹명을 입력해주세요"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="YearSemester">연도</label>
            <select id="Year" value={year} onChange={handleYearChange} required>
              <option value="">연도</option>
              <option value="2024">2024</option>
              <option value="2023">2023</option>
              <option value="2022">2022</option>
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="YearSemester">학기</label>

            <select
              id="Semester"
              value={semester}
              onChange={handleSemesterChange}
              required
            >
              <option value="">학기</option>
              <option value="FIRST">1학기</option>
              <option value="SUMMER">하계계절학기</option>
              <option value="SECOND">2학기</option>
              <option value="WINTER">동계계절학기</option>
            </select>
          </div>

          {/* <button type="submit" className='group-creation-btn'>그룹 생성</button>

        {showPopup &&(
                    <div className="popup-overlay">
                        <div className="popup">
                            <h3></h3>
                            <p>그룹이 생성되었습니다</p>
                            <button onClick={handlePopupClose}>확인</button>
                        </div>
                    </div>
                )}     */}
          <button type="submit" className="group-creation-btn">
            그룹 생성
          </button>
        </form>
      </div>
    </div>
  );
};

export default Group_creation;
