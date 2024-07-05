
import { Link } from "react-router-dom";
import backgroundImage from './Cognira-logo-blue.png';
import React, { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from '../axiosConfig'; 
import '../App.css';
export const CustumGridOptions = (props) => {
    const [option, setOption] = useState('');
    const [tenantId,setTenantId] = useState("");
    const [intersectionId,setIntersectionId] = useState("Any");
    const [p1Id,setP1Id] = useState("Any");
    const [c1Id,setC1Id] = useState("Any");
    const [tenantIds, setTenantIds] = useState([]);
    const [intersectionIds, setIntersectionIds] = useState([]);
    const [p1Ids, setP1Ids] = useState([]);
    const [c1Ids, setC1Ids] = useState([]);
    const [InputOption, setInputOption] = useState('InputOption1');
    const [inputFields, setInputFields] = useState([{ value: "" }]);
    const navigate = useNavigate();
    const handleInputOptionChange = (e) => {
      setInputOption(e.target.value);
    };
    let  backEndpoint = process.env.REACT_APP_BACK_ENDPOINT;
    if (! backEndpoint) {
        // If BACK_ENDPOINT is not set, use localhost
        backEndpoint='localhost:8080';
    }
    function generateRegex() {

        // Replace 'Any' with appropriate regex pattern
        const tenantId2 = (tenantId === 'Any') ? '[a-zA-Z0-9]+' : tenantId;
        const intersectionId2 = (intersectionId === 'Any') ? '[a-zA-Z0-9]+' : intersectionId;
        const p1Id2 = (p1Id === 'Any') ? '[a-zA-Z0-9]+' : p1Id;
        const c1Id2 = (c1Id === 'Any') ? '[a-zA-Z0-9]*' : c1Id;
        
        return encodeURIComponent(`${tenantId2}-${intersectionId2}-${p1Id2}-${c1Id2}`);
    }
    useEffect(() => {
        getPersitentIds();
      }, []);
      const getPersitentIds= async () => {
        const Set1 = new Set();
        const Set2 = new Set();
        const Set3 = new Set();
        const Set4 = new Set();

        const baseURL  = `http://${backEndpoint}/getIds`;
        const response = await axios.get(baseURL);
        const data = await response.data;
        data.forEach((str) => {
            const [p1, p2, p3, p4] = str.split("-");
            Set1.add(p1);
            Set2.add(p2);
            Set3.add(p3);
            Set4.add(p4);
          })
          Set2.add("Any")
          Set3.add("Any")
          Set4.add("Any")
          setTenantIds(Array.from(Set1));
          setIntersectionIds(Array.from(Set2));
          setP1Ids(Array.from(Set3));
          setC1Ids(Array.from(Set4));
      }
    const handleSubmit = async (e) => {
        console.log(generateRegex())
        if(InputOption=='InputOption1')
        navigate(`/GridComponent?option=${option}&persistentId=${generateRegex()}&skip=${0}&NbrOf=${1000000000}&getAll=true` );
        else{
            const l=inputFields.map(obj => obj.value)
            console.log(l)
            navigate(`/FieldGrid?option=${option}&persistentId=${generateRegex()}`,{ state:{l} } );
        }
    }
  
    const handleOptionChange = (e) => {
        setOption(e.target.value);
    };
    const handleAddFields = (event) => {
        event.preventDefault();
        setInputFields([...inputFields, { value: "" }]);
        console.log(inputFields);
      };
    
      // Function to remove an input field by index
      const handleRemoveFields = (event, index) => {
        event.preventDefault(); 
        const newInputFields = [...inputFields];
        newInputFields.splice(index, 1);
        setInputFields(newInputFields);
      };
    
      // Function to update the value of an input field
      const handleValueChange = (index, event) => {
        const values = [...inputFields];
        values[index].value = event.target.value;
        setInputFields(values);
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
                    <option value="allEvents">Events</option>
                    <option value="allStates">State</option>
                </select>
                <div style={{ display: "flex", alignItems: "center" }}>
                <select id="dropdown" value={tenantId} onChange={(e) => {setTenantId(e.target.value)}} style={{ marginRight: '10px' }} required>
                    <option value="">Select Id</option>
                    {tenantIds.map((option, index) => (
                    <option key={index} value={option} >{option}</option>
                    ))}
                    
                </select>
                <select id="dropdown" value={intersectionId} onChange={(e) => {setIntersectionId(e.target.value)}} style={{ marginRight: '10px' }} required>
                    
                    {intersectionIds.map((option, index) => (
                    <option key={index} value={option} >{option}</option>
                    ))}
                </select>
                <select id="dropdown" value={p1Id} onChange={(e) => {setP1Id(e.target.value)}} style={{ marginRight: '10px' }} required>
                    
                    {p1Ids.map((option, index) => (
                    <option key={index} value={option} >{option}</option>
                    ))}
                </select>
                <select id="dropdown" value={c1Id} onChange={(e) => {setC1Id(e.target.value)}} required>
                    
                    {c1Ids.map((option, index) => (
                    <option key={index} value={option} >{option}</option>
                    ))}
                </select>
                </div>
                <label>
                    Monitor All Fields Changes  
                    <input 
                    type="radio" 
                    value="InputOption1" 
                    className="form-input" 
                    checked={InputOption === 'InputOption1'} 
                    onChange={handleInputOptionChange} 
                                    />
                </label>
                <label>
                Select Fields To monitor
                <input 
                type="radio" 
                value="InputOption2" 
                className="form-input" 
                checked={InputOption === 'InputOption2'} 
                onChange={handleInputOptionChange} 
                />
                </label>
                <div>       {InputOption === 'InputOption2' && (
                <div className="container">
             

      {inputFields.map((inputField, index) => (
        <div className="input-container" key={index}>
          <input
            type="text"
            placeholder="Enter a value"
            value={inputField.value}
            onChange={(e) => handleValueChange(index, e)}
          />

          <button className="delete-btn" onClick={(event) => handleRemoveFields(event, index)}>
            <span class="material-symbols-outlined delete-icon">delete</span>
          </button>
        </div>
      ))}

      <button className="add-btn" onClick={handleAddFields}>
       Add Field
      </button>
      </div> )}
    </div>
                <button type="submit">Monitor</button>
            </form>
        </div>
    </div>
    </div>
  );
}
