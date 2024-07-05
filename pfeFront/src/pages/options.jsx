
import { Link } from "react-router-dom";
import backgroundImage from './Cognira-logo-blue.png';
import React, { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from '../axiosConfig'; 
import '../App.css';
export const Options = (props) => {
    const [option, setOption] = useState('');
    const [skip, setSkip] = useState(0);
    const [latest, setLatest] = useState(0);
    const [NbrOf, setNbrOf] = useState(1);
    const [persistentId, setPersistentId] = useState("");
    const [nbrOfEvents, setNbrOfEvents] = useState(0);
    const [ids, setPersistentIds] = useState([]);
    const [InputOption, setInputOption] = useState('InputOption1');
    const navigate = useNavigate();
    const handleInputOptionChange = (e) => {
      setInputOption(e.target.value);
    };
    const handleLatestChange = (e) => {
      const newValue = parseInt(e.target.value);
      if (!isNaN(newValue) && newValue >= 0 ) {
        setLatest(parseInt(newValue));
      }
  };
  let  backEndpoint = process.env.REACT_APP_BACK_ENDPOINT;
  if (! backEndpoint) {
      // If BACK_ENDPOINT is not set, use localhost
      backEndpoint='localhost:8080';
  }
  function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
    useEffect(() => {
        getPersitentIds();
      }, []);
      const getPersitentIds= async () => {
        const baseURL  = `http://${backEndpoint}/getIds`;
        const response = await axios.get(baseURL);
        const data = await response.data;
        setPersistentIds(data)
      }
      const getNumberOfEvents= async (id) => {
        const baseURL  = `http://${backEndpoint}/getNumberOfEvents?persistentId=${id}`;
        const response = await axios.get(baseURL);
        const data = await response.data;
        setNbrOfEvents(parseInt(data))
      }
    const handleSubmit = async (e) => {
     
        if(InputOption === 'InputOption1'){
        navigate(`/GridComponent?option=${option}&persistentId=${persistentId}&skip=${skip}&NbrOf=${NbrOf-skip+1}` );}
        else{
          
           console.log(nbrOfEvents)
          navigate(`/GridComponent?option=${option}&persistentId=${persistentId}&skip=${Math.max(0,nbrOfEvents-latest+1)}&NbrOf=${latest}` );
        }
    };
    const handleOptionChange = (e) => {
        setOption(e.target.value);
    };

    const handleskipChange = (e) => {
        const newValue = parseInt(e.target.value);
        if (!isNaN(newValue) && newValue >= 0 && newValue<=NbrOf) {
        setSkip(parseInt(newValue));
        }
    };
    const handleIdChange = (e) => {
        setPersistentId(e.target.value)
        getNumberOfEvents(e.target.value);
    };

    const handleNbrOfChange = (e) => {
        const newValue = parseInt(e.target.value);
        if (!isNaN(newValue) && newValue >= skip) {
        setNbrOf(parseInt(e.target.value));
        }
    };
  return (
    <div className="home-container">
        <div className="auth-form-container">
        <a href="/Monitor">
      <div className="image-container">
        <img src={backgroundImage} alt="Background" style={{ width: '400px', height: 'auto' }} />
      </div>
      </a>
      <h1>Choose Your Options</h1>
      <div >
            <form className="login-form" onSubmit={handleSubmit} >
            
                <label htmlFor="option">Choose an option:</label>
                <select id="option" value={option} onChange={handleOptionChange} required>
                    <option value="">Select an option</option>
                    <option value="events">Events</option>
                    <option value="states">State</option>
                </select>
                <select id="dropdown" value={persistentId} onChange={handleIdChange} required>
        <option value="">Select Id</option>
        {ids.map((option, index) => (
          <option key={index} value={option} >{option}</option>
        ))}
      </select>
      <label>
Select Range  
<input 
  type="radio" 
  value="InputOption1" 
  className="form-input" 
  checked={InputOption === 'InputOption1'} 
  onChange={handleInputOptionChange} 
/>
</label>

      <div style={{ display: "flex", alignItems: "center" }}>
                <label htmlFor="skip"  style={{ marginRight: "20px" }}>From </label>
                <input type="number" id="skip" value={skip} onChange={handleskipChange} disabled={InputOption === 'InputOption2'}/>
                <label htmlFor="NbrOf"  style={{ marginRight: "20px" ,marginLeft: "10px" }}>To </label>
                <input type="number" id="NbrOf" value={NbrOf} onChange={handleNbrOfChange} disabled={InputOption === 'InputOption2'}/>
               
                </div>
                <label>
                Select Latest
<input 
  type="radio" 
  value="InputOption2" 
  className="form-input" 
  checked={InputOption === 'InputOption2'} 
  onChange={handleInputOptionChange} 
/>
<input type="number" id="latest" value={latest} onChange={handleLatestChange} disabled={InputOption === 'InputOption1'}/>
</label>
                <button type="submit">Monitor</button>
            </form>
        </div>
    </div>
    </div>
  );
}
