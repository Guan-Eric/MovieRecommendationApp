import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import ToWatchPage from './ToWatchPage';
import SeenPage from './SeenPage';
import AvoidPage from './AvoidPage';
import RecommendationsPage from './RecommendationsPage';
import './App.css'; 


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<ToWatchPage />} />
        <Route path="/to-watch" element={<ToWatchPage />} />
        <Route path="/seen" element={<SeenPage />} />
        <Route path="/avoid" element={<AvoidPage />} />
        <Route path="/recommendations" element={<RecommendationsPage />} />
      </Routes>
    </Router>
  );
}

export default App;
