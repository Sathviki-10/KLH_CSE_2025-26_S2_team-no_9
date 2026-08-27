import React, { useState, useEffect } from 'react';
import { Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import Home from './pages/Home';
import JobResults from './pages/JobResults';
import JobDetails from './components/JobDetails';
import Profile from './pages/Profile';
import Login from './pages/Login';
import { saveJob, unsaveJob, getSavedJobs } from './services/api';

const App = () => {
  const [user, setUser] = useState(null);
  const [savedJobIds, setSavedJobIds] = useState([]);

  useEffect(() => {
    const stored = localStorage.getItem('user');
    if (stored) {
      const userData = JSON.parse(stored);
      setUser(userData);
      loadSavedJobs(userData.id);
    }
  }, []);

  const loadSavedJobs = async (userId) => {
    try {
      const data = await getSavedJobs(userId);
      const ids = data.saved_jobs.map(j => j.id);
      setSavedJobIds(ids);
    } catch (err) {
      console.error(err);
    }
  };

  const handleLogin = (userData) => {
    setUser(userData);
    localStorage.setItem('user', JSON.stringify(userData));
    loadSavedJobs(userData.id);
  };

  const handleLogout = () => {
    setUser(null);
    setSavedJobIds([]);
    localStorage.removeItem('user');
  };

  const handleSaveJob = async (userId, jobId) => {
    try {
      await saveJob(userId, jobId);
      setSavedJobIds(prev => [...prev, jobId]);
    } catch (err) {
      console.error(err);
    }
  };

  const handleUnsaveJob = async (userId, jobId) => {
    try {
      await unsaveJob(userId, jobId);
      setSavedJobIds(prev => prev.filter(id => id !== jobId));
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="app">
      <Header user={user} onLogout={handleLogout} />
      <main className="main-content">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/jobs" element={<JobResults user={user} />} />
          <Route
            path="/jobs/:id"
            element={
              <JobDetails
                user={user}
                onSaveJob={handleSaveJob}
                onUnsaveJob={handleUnsaveJob}
                savedJobIds={savedJobIds}
              />
            }
          />
          <Route path="/profile" element={<Profile user={user} />} />
          <Route path="/login" element={<Login onLogin={handleLogin} />} />
        </Routes>
      </main>
    </div>
  );
};

export default App;
