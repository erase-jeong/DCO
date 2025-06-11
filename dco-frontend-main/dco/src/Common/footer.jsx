import React from 'react';
import './footer.css';

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-content">
        <p>DCO</p>
        <hr className="footer-divider" />
        <div className="footer-info">
          {/* <img src="/" alt="Logo" className="footer-logo" /> */}
          <div>
            {/* <p>
              승학캠퍼스(대학본부) 49315. 부산광역시 사하구 낙동대로 550번길
              37(하단동) 대표전화 : 051-200-6114
            </p>
            <p>구덕캠퍼스 49201. 부산광역시 서구 대신공원로 32(동대신동 3가)</p>
            <p>부민캠퍼스 49236. 부산광역시 서구 구덕로 225(부민동 2가)</p> */}
            <p>COPYRIGHT© 2024 DevWorks. ALL RIGHTS RESERVED.</p>
          </div>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
