import backgroundImage from './Cognira-logo-blue.png';
import React, { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import '../App.css';
export const TenantHistoryOptions = (props) => {
    const [option, setOption] = useState('');

    const navigate = useNavigate();


    const handleSubmit = async (e) => {
        
        navigate(`/TenantGrid?option=${option}` );
    };
    const handleOptionChange = (e) => {
        setOption(e.target.value);
    };

  return (
    <div className="home-container">
        <div className="auth-form-container">
        <a href="/Monitor">
      <div className="image-container">
        <img src={backgroundImage} alt="Background" style={{ width: '400px', height: 'auto' }} />
      </div>
      </a>
      <h1>Choose Your Tenant</h1>
      <div >
            <form className="login-form" onSubmit={handleSubmit} >
               
                <label htmlFor="Tenant"  style={{ marginRight: "20px" ,marginLeft: "10px" }}>Tenant </label>
                <input type="string" id="Tenant" value={option} onChange={(e) => { setOption(e.target.value);}} />
    
                <button type="submit">Monitor</button>
            </form>
        </div>
    </div>
    </div>
  );
}