import React, { useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import './App.css';
import { Login } from "./pages/Login";
import { Register } from "./pages/Register";
import { Home } from "./pages/home";
import { GridComponent } from "./pages/GridComponent";
import { Options } from "./pages/options";
import { Monitor } from "./pages/Monitor";
import { TenantHistoryOptions } from "./pages/TenantHistoryOptions";
import { TenantGrid } from "./pages/TenantGrid";
import { CustumGridOptions } from "./pages/CustumGridOptions";
import { FieldGrid } from "./pages/FieldGrid";
function App() {
  const [currentForm, setCurrentForm] = useState('login');

  const toggleForm = (formName) => {
    setCurrentForm(formName);
  }

  return (
    <Router>
    <div className="App">
      <Routes>
      <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/GridComponent" element={<GridComponent />} />
        <Route path="/Monitor" element={<Monitor />} />
        <Route path="/Options" element={<Options />} />
        <Route path="/TenantHistoryOptions" element={<TenantHistoryOptions />} />
        <Route path="/TenantGrid" element={<TenantGrid />} />
        <Route path="/CustumGridOptions" element={<CustumGridOptions />} />
        <Route path="/FieldGrid" element={<FieldGrid />} />
      </Routes>
    </div>
  </Router>
  );
}

export default App;
