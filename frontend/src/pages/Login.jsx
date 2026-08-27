import React, { useState, useEffect } from 'react';
import { Link, Navigate } from 'react-router-dom';
import { createUser, login } from '../services/api';
import './Auth.css';

const Login = ({ onLogin }) => {
  const [isRegister, setIsRegister] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    location: 'Bangalore',
    experience: '2 years',
    preferred_job_type: 'Full-time'
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      if (isRegister) {
        const data = await createUser(formData);
        const newUser = data.data || { id: Date.now(), name: formData.name, email: formData.email };
        onLogin(newUser);
      } else {
        const data = await login(formData.email, formData.password);
        if (data.success && data.data) {
          onLogin(data.data);
        } else {
          setError(data.message || 'Invalid credentials');
        }
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Something went wrong');
    } finally {
      setLoading(false);
    }
  };

  if (isRegister) {
    return (
      <div className="auth-page">
        <div className="auth-card">
          <h1>Create Account</h1>
          <p className="auth-subtitle">Join Smart Job Portal</p>
          {error && <div className="auth-error">{error}</div>}
          <form onSubmit={handleSubmit}>
            <input
              type="text"
              placeholder="Full Name"
              required
              value={formData.name}
              onChange={e => setFormData({...formData, name: e.target.value})}
            />
            <input
              type="email"
              placeholder="Email"
              required
              value={formData.email}
              onChange={e => setFormData({...formData, email: e.target.value})}
            />
            <input
              type="password"
              placeholder="Password"
              required
              value={formData.password}
              onChange={e => setFormData({...formData, password: e.target.value})}
            />
            <select value={formData.location} onChange={e => setFormData({...formData, location: e.target.value})}>
              <option value="Bangalore">Bangalore</option>
              <option value="Hyderabad">Hyderabad</option>
              <option value="Chennai">Chennai</option>
              <option value="Pune">Pune</option>
              <option value="Mumbai">Mumbai</option>
              <option value="Delhi">Delhi</option>
            </select>
            <button type="submit" disabled={loading}>
              {loading ? 'Creating...' : 'Sign Up'}
            </button>
          </form>
          <p className="auth-switch">
            Already have an account? <button onClick={() => { setIsRegister(false); setError(''); }}>Sign In</button>
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="auth-page">
      <div className="auth-card">
        <h1>Welcome Back</h1>
        <p className="auth-subtitle">Sign in to Smart Job Portal</p>
        {error && <div className="auth-error">{error}</div>}
        <form onSubmit={handleSubmit}>
          <input
            type="email"
            placeholder="Email"
            required
            value={formData.email}
            onChange={e => setFormData({...formData, email: e.target.value})}
          />
          <input
            type="password"
            placeholder="Password"
            required
            value={formData.password}
            onChange={e => setFormData({...formData, password: e.target.value})}
          />
          <button type="submit" disabled={loading}>
            {loading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>
        <p className="auth-switch">
          Don't have an account? <button onClick={() => { setIsRegister(true); setError(''); }}>Sign Up</button>
        </p>
      </div>
    </div>
  );
};

export default Login;
