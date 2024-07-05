import React, { useState } from "react";
import axios from "axios";
import backgroundImage from './Cognira-logo-blue.png';
import { useNavigate } from 'react-router-dom';
import { Link } from "react-router-dom";

export const Login = (props) => {
    const [email, setEmail] = useState('');
    const [pass, setPass] = useState('');
    const [showError, setShowError] = useState(false);
    const [loading, setLoading] = useState(false); 
    const [tenant_id, settenant_id] = useState(-1); 
    const navigate = useNavigate();
    const backEndpoint = process.env.REACT_APP_BACK_ENDPOINT;
    const handleSubmit = async (e) => {
        e.preventDefault();
        let baseURL;
        setLoading(true);
        if (backEndpoint) {
            // If BACK_ENDPOINT is set, use it in the URL
             baseURL  = `http://${backEndpoint}/login`;
        } else {
            // If BACK_ENDPOINT is not set, use localhost
             baseURL  = 'http://localhost:8080/login';
        }
        try {
            const response = await axios.post(baseURL, {
                "email": email,
                "password": pass
            });
            setLoading(false);
            if (response.status === 200) {
         
                axios.defaults.headers.common['Authorization'] = response.data;
                localStorage.setItem('token', response.data);
                localStorage.setItem('tenant_id', response.data);
                navigate('/Monitor');
            } else {
 
            }
        } catch (error) {
            setLoading(false);
     
            setShowError(true);
            console.error('Login failed:', error);
        }
    };

    return (
        <div>
            {loading ? (
                <div className="loading-spinner">
                    <svg className="spinner" width="50px" height="50px" viewBox="0 0 66 66" xmlns="http://www.w3.org/2000/svg">
                        <circle className="path" fill="none" strokeWidth="6" strokeLinecap="round" cx="33" cy="33" r="30"></circle>
                    </svg>
                </div>
            ) : (
                <div className="auth-form-container">
                      <a href="/">
                    <div className="image-container">
                        <img src={backgroundImage} alt="Background" style={{ width: '200px', height: 'auto' }} />
                    </div>
                    </a>
                    <h2>Login</h2>
                    <form className="login-form" onSubmit={handleSubmit}>
                        <label htmlFor="email">email</label>
                        <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" placeholder="youremail@gmail.com" id="email" name="email" />
                        <label htmlFor="password">password</label>
                        <input value={pass} onChange={(e) => setPass(e.target.value)} type="password" placeholder="********" id="password" name="password" />
                        <button type="submit">Log In</button>
                        {showError && <p className="error-message">Credentials are incorrect.</p>}
                    </form>
                    <button className="link-btn">
                        <Link to="/register">Don't have an account? Register here.</Link>
                    </button>
                </div>
            )}
        </div>
    );
}
