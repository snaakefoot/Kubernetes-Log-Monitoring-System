
import React, { useEffect, useState } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import {  useLocation } from 'react-router-dom';
import { Dialog, DialogTitle, DialogContent, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, TextField, IconButton } from '@mui/material';
import Switch from '@mui/material/Switch';
import backgroundImage from './Cognira-logo-blue.png';
import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import NavigateBeforeIcon from '@mui/icons-material/NavigateBefore';
import ExitToAppOutlinedIcon from '@mui/icons-material/ExitToAppOutlined';
import RadioButtonCheckedIcon from '@mui/icons-material/RadioButtonChecked';
import { useNavigate } from 'react-router-dom';
import axios from '../axiosConfig'; 
import '../App.css';
export const TenantGrid = () => {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const [rows, setRows] = useState([]);
  const option=queryParams.get('option');
  const persitentId=queryParams.get('persistentId');
  const nbOf=queryParams.get('NbrOf');
  const token = localStorage.getItem('token');// Securely obtained from your auth flow
  const navigate = useNavigate();
  let  backEndpoint = process.env.REACT_APP_BACK_ENDPOINT;
  if (! backEndpoint) {
      // If BACK_ENDPOINT is not set, use localhost
      backEndpoint='localhost:8080';
  }
  function getTimePassed(lastUpdate) {
    const parsedTime = new Date("20" + lastUpdate.substring(0, 17).replace(/-/g, "/"));
    const currentTime = new Date();
    const timeDifferenceMs = currentTime - parsedTime;

    const millisecondsInSecond = 1000;
    const millisecondsInMinute = millisecondsInSecond * 60;
    const millisecondsInHour = millisecondsInMinute * 60;
    const millisecondsInDay = millisecondsInHour * 24;

    const daysPassed = Math.floor(timeDifferenceMs / millisecondsInDay);
    const hoursPassed = Math.floor((timeDifferenceMs % millisecondsInDay) / millisecondsInHour);
    const minutesPassed = Math.floor((timeDifferenceMs % millisecondsInHour) / millisecondsInMinute);
    const secondsPassed = Math.floor((timeDifferenceMs % millisecondsInMinute) / millisecondsInSecond);

    if (daysPassed > 0) {
        return `${daysPassed} day${daysPassed > 1 ? 's' : ''} ago`;
    } else if (hoursPassed > 0) {
        return `${hoursPassed} hour${hoursPassed > 1 ? 's' : ''} ago`;
    } else if (minutesPassed > 0) {
        return `${minutesPassed} minute${minutesPassed > 1 ? 's' : ''} ago`;
    } else {
        return `${secondsPassed} second${secondsPassed > 1 ? 's' : ''} ago`;
    }
}
  const fetchData = async () => {
    let baseURL;
    
     try {
         baseURL  = `http://${backEndpoint}/tenant?tenant=${option}`;
    

      const response = await axios.get(baseURL);
     
      const data = await response.data;
      console.log(data)
      setRows(data); // Assuming your backend returns an array of rows
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };
  useEffect(() => {
    console.log(getTimePassed("24-05-08 10:39:25.751285"))
    fetchData();
  }, []);
  
  const handleRowClick = (row) => {
    navigate(`/GridComponent?option=events&persistentId=${`${row.id}-${row.intersectionId}-${row.p1}-${row.c1}`}&skip=${0}&NbrOf=${1000000000}` );
  };
  const handleRowClick2 = (row) => {
    navigate(`/GridComponent?option=states&persistentId=${`${row.id}-${row.intersectionId}-${row.p1}-${row.c1}`}&skip=${0}&NbrOf=${1000000000}` );
  };

 
  const columns = [

      { field: 'p1', headerName: 'P1', width: 70, headerAlign: 'center',align:'center' },   
      { field: 'c1', headerName: 'C1', width: 100  , headerAlign: 'center',align:'center'},   
      { field: 'intersectionId', headerName: 'Intersection Id', width: 120 , headerAlign: 'center',align:'center'},   
      { field: 'lastUpdate', headerName: 'Last updated', width: 200, headerAlign: 'center', align: 'center', valueGetter: params =>getTimePassed(params.value) },
      { field: 'numberOfUpdates', headerName: 'Events Count', width: 100 , headerAlign: 'center',align:'center'},    
   
      {
        field: 'events',
        headerName: 'Events',
         headerAlign: 'center',
        width: 170,
        renderCell: (params) => (
          <IconButton   onClick={() => handleRowClick(params.row) } sx={{
            p: 1, // Padding
            fontSize: '1rem', // Font size
            minWidth: 'auto', // Minimum width
          }} >View all Events  
          <ExitToAppOutlinedIcon  />
          </IconButton >
        ),
      },
      {
        field: 'states',
        headerName: 'States' , headerAlign: 'center',
        width: 170,
        renderCell: (params) => (
          <IconButton   onClick={() => handleRowClick2(params.row) } sx={{
            p: 1, // Padding
            fontSize: '1rem', // Font size
            minWidth: 'auto', // Minimum width
          }} >View all States  
          <ExitToAppOutlinedIcon  />
          </IconButton >
        ),
      },
    ];
  
  return (
    <div>
                        <a href="/Monitor">
      <div className="image-container" style={{marginTop:'30px'}} >
                <img src={backgroundImage} alt="Background" style={{ width: '400px', height: 'auto' }} />
                
      </div>
      </a>
      
      <div style={{ textAlign: 'center', marginBottom: '10px', color: 'white', fontSize: '24px' }}>
       <h1>  History of Tenant {option}</h1>

      </div>
     
    <div style={{ width: '100%', backgroundColor: 'white' }}>
      <DataGrid
        rows={rows}
        columns={columns}
        pageSize={5}
        getRowId={(row) => `${row.id}-${row.intersectionId}-${row.p1}-${row.c1}`}
        
      />
    </div>
    </div>
  );
}
