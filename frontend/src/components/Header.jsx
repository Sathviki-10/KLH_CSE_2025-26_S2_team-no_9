import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './Header.css';

const Header = ({ user, onLogout }) => {
  const [menuOpen, setMenuOpen] = useState(false);
  const navigate = useNavigate();

  return (
    <header className="header">
      <div className="header-container">
        <Link to="/" className="logo">
          <div className="logo-icon">SJ</div>
          <div className="logo-text">
            <h1>Smart Job Portal</h1>
            <span>Search System</span>
          </div>
        </Link>
        <nav className={`nav ${menuOpen ? 'nav-open' : ''}`}>
          <Link to="/" className="nav-link" onClick={() => setMenuOpen(false)}>Home</Link>
          <Link to="/jobs" className="nav-link" onClick={() => setMenuOpen(false)}>Browse Jobs</Link>
          {user ? (
            <div className="user-menu">
              <Link to="/profile" className="nav-link" onClick={() => setMenuOpen(false)}>Profile</Link>
              <button onClick={onLogout} className="btn-logout">Logout</button>
            </div>
          ) : (
            <Link to="/login" className="btn-login" onClick={() => setMenuOpen(false)}>Sign In</Link>
          )}
        </nav>
        <button className="menu-toggle" onClick={() => setMenuOpen(!menuOpen)}>
          <span></span><span></span><span></span>
        </button>
      </div>
    </header>
  );
};

export default Header;
