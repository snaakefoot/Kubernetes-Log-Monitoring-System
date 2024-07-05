import React from "react";
import { Link } from "react-router-dom";
import backgroundImage from './Cognira-logo-blue.png';

export const Monitor = (props) => {
  return (
    <div className="home-container">
                      <a href="/">
      <div className="image-container">
        <img src={backgroundImage} alt="Background" style={{ width: '400px', height: 'auto' }} />
      </div>
      </a>
      <h1>Choose How To monitor</h1>
      <div className="button-container">
        <Link to="/options" className="light-button">
          <button style={{ width: '300px', height: '60px' }}>Live Monitoring By id</button>
        </Link>
        <Link to="/CustumGridOptions" className="light-button">
          <button style={{ width: '300px', height: '60px' }}>Custom Historic monitoring</button>
        </Link>
      </div>
      <Link to="/TenantHistoryOptions" className="light-button">
          <button style={{ width: '300px', height: '60px' }}>View Tenant History</button>
        </Link>
    </div>
  );
}
