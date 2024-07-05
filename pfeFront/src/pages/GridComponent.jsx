
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
import '../App.css';
export const GridComponent = () => {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const [rows, setRows] = useState([]);
  const [popupOpen2, setPopupOpen2] = useState(false);
  const [selectedRow, setSelectedRow] = useState({});
  const [selectedRowK8s, setSelectedRowK8s] = useState({});
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedRowIndex, setSelectedRowIndex] = useState(0);
  const [popupOpen, setPopupOpen] = useState(false);
  const [checked, setChecked] = useState(false);
  const [nbOf, setNbOf] = useState(queryParams.get('NbrOf'));
  const [liveRecord, setLiveRecord] = useState("Monitoring"); 
  const skip=queryParams.get('skip');
  const option=queryParams.get('option');
  const persistentId=queryParams.get('persistentId');
  const getAll=queryParams.get('getAll');
  const token = localStorage.getItem('token');// Securely obtained from your auth flow
  let  backEndpoint = process.env.REACT_APP_BACK_ENDPOINT;
  if (! backEndpoint) {
      // If BACK_ENDPOINT is not set, use localhost
      backEndpoint='localhost:8080';
  }
  
  const ws = new WebSocket(`ws://${backEndpoint}/${option}/${encodeURIComponent(persistentId)}/${skip}/${nbOf}?token=${encodeURIComponent(token)}`);
  useEffect(() => {
    ws.onopen = () => {
      console.log('WebSocket Connected');    
    };
    ws.onmessage = (event) => {
      const newData = JSON.parse(event.data);
      console.log(newData)
      setRows((prevRows) => [...prevRows, {
        Row_Number :prevRows.length ,
        Date_Time: newData[0],
        Pod_Name: newData[2].pod_name,
        Node: newData[2].host,
        Metrics:  newData[1],
        Kubernates_Data:   newData[2],
        intersectionId:  newData[3],
        p1:  newData[4],
        c1:  newData[5],
      }]);
    };
    console.log(rows )
    ws.onerror = (error) => {
      console.error('WebSocket Error: ', error);
      setLiveRecord("Monitored")
     
      console.log(rows )
    };
    ws.onclose =() =>{
      setLiveRecord("Monitored");
      
      console.log( rows)
    }
    return () => {
      if (ws.readyState === 1) { // <-- This is important
        ws.close();
    }
    };
  }, []);
  
  const handleRowClick = (row) => {
    setSelectedRowIndex(row.Row_Number);
    setSelectedRow(row.Metrics);
    setPopupOpen(true);
  };
  const handleRowClick2 = (row) => {
    setSelectedRowIndex(row.Row_Number);
    console.log(row.Kubernates_Data)
    setSelectedRowK8s(row.Kubernates_Data)
    setPopupOpen2(true);
  };

  const handleClosePopup2 = () => {
    setPopupOpen2(false);
  };

  const handleClosePopup = () => {
    setPopupOpen(false);
  };

  const handleNextRow = () => {
    const newIndex =(selectedRowIndex < rows.length - 1 ? selectedRowIndex + 1 : selectedRowIndex)
    setSelectedRowIndex(newIndex);
    setSelectedRow(rows[newIndex].Metrics);
  };

  const handlePreviousRow = () => {
    const newIndex =((selectedRowIndex > 0 ? selectedRowIndex - 1 : selectedRowIndex))
    setSelectedRowIndex(newIndex);
    setSelectedRow(rows[newIndex].Metrics);
  };
  const handleChange = () => {
    setChecked((prev) => !prev);
  };

  const filteredKeys = Object.keys(selectedRow).filter((key) =>
  (key.toLowerCase().includes(searchQuery.toLowerCase()) && (!checked || selectedRow[key]))
    
);
  const columns = [
      { field: 'Row_Number', headerName: 'Row', width: 20  ,headerAlign: 'center',align:'center'},
      { field: 'Date_Time', headerName: 'Date_Time', width: 200 ,headerAlign: 'center',align:'center'},
      { field: 'intersectionId', headerName: 'intersectionId', width: 150 ,headerAlign: 'center',align:'center'},    
      { field: 'p1', headerName: 'p1', width: 50,headerAlign: 'center',align:'center' },    
      { field: 'c1', headerName: 'c1', width: 100 ,headerAlign: 'center',align:'center'},  
      { field: 'Pod_Name', headerName: 'Pod_Name', width: 250,headerAlign: 'center',align:'center' },
      { field: 'Node', headerName: 'Node', width: 250 ,headerAlign: 'center',align:'center'},      
      {
        field: 'Metrics',
        headerName: 'Metrics',
        headerAlign: 'center',align:'center',
        width: 100,
        renderCell: (params) => (
          <IconButton   onClick={() => handleRowClick(params.row) } sx={{
            p: 1, // Padding
            fontSize: '1rem', // Font size
            minWidth: 'auto', // Minimum width
          }} >View  
          <ExitToAppOutlinedIcon  />
          </IconButton >
        ),
      },
      {
        field: 'Kubernates_Data',
        headerName: 'MetaData',
        width: 100,
        renderCell: (params) => (
          <IconButton    onClick={() => handleRowClick2(params.row)} sx={{
              p: 1, // Padding
              fontSize: '1rem', // Font size
              minWidth: 'auto', // Minimum width
            }} > View 
          <ExitToAppOutlinedIcon  />
          </IconButton >
        ),
      },
    ];
  const stringifyValue = (value) => {
    if (typeof value === 'object') {
      return JSON.stringify(value);
    }

    return value.toString();;
  };
  
  return (
    <div>
                      <a href="/Monitor">
      <div className="image-container" style={{marginTop:'30px'}} >
                <img src={backgroundImage} alt="Background" style={{ width: '400px', height: 'auto' }} />      
      </div>
      </a>
      <div style={{ textAlign: 'center', marginBottom: '10px', color: 'white', fontSize: '24px' }}>
       <h1>  {liveRecord} {option} Between  {parseInt(skip)+1} and  {liveRecord!="Monitoring" ? parseInt(skip) + rows.length : "Current Ones"}         {liveRecord=="Monitoring" && <RadioButtonCheckedIcon fontSize="xxxx-large" style={{ color: 'red' }}/>}</h1>

      </div>
     
    <div style={{ width: '100%', backgroundColor: 'white' }}>
      <DataGrid
        rows={rows}
        columns={columns}
        pageSize={5}
        getRowId={(row) => row.Row_Number}
      />
      <Dialog open={popupOpen} onClose={handleClosePopup} maxWidth="md" fullWidth >
      <Paper style={{ maxHeight: '650px',minHeight: '650px', overflow: 'auto' }}>
        <DialogTitle>Metrics</DialogTitle>
        <DialogContent>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
            <IconButton onClick={handlePreviousRow}>
              <NavigateBeforeIcon />
            </IconButton>

            <TextField
              label="Search Key"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              variant="outlined"
              fullWidth
              margin="normal"
            />
            <IconButton onClick={handleNextRow}>
              <NavigateNextIcon />
            </IconButton>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end', marginBottom: '10px' }}>
  <span style={{ marginRight: '10px' , fontWeight: 'bold' }}>Hide Nulls</span>
      <Switch
        checked={checked}
        onChange={handleChange}
        inputProps={{ 'aria-label': 'controlled' }}
        color="primary"
      />
    </div>
          {rows[selectedRowIndex] && (
            <TableContainer component={Paper}>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Key</TableCell>
                    <TableCell>Value</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {filteredKeys.map((key) => (
                    <TableRow key={key}>
                      <TableCell style={{ width: '50%' }}>{key}</TableCell>
                      <TableCell style={{ width: '50%' }}>{stringifyValue(selectedRow[key])}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          )}
        </DialogContent>
        </Paper>
      </Dialog>
      <Dialog open={popupOpen2} onClose={handleClosePopup2}>
        <DialogTitle>Kubernates Metadata</DialogTitle>
        <DialogContent>
          {selectedRowK8s && (
            <TableContainer component={Paper}>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Key</TableCell>
                    <TableCell>Value</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  { Object.keys(selectedRowK8s).map((key) => (
                    <TableRow key={key}>
                      <TableCell style={{ width: '50%' }}>{key}</TableCell>
                      <TableCell style={{ width: '50%' }}>{stringifyValue(selectedRowK8s[key])}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          )}
        </DialogContent>
      </Dialog>
    </div>
    </div>
  );
}
