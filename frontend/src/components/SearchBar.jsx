import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { LOCATIONS, SKILLS } from '../utils/constants';
import './SearchBar.css';

const SearchBar = ({ initialQuery = '', initialLocation = '' }) => {
  const [query, setQuery] = useState(initialQuery);
  const [location, setLocation] = useState(initialLocation);
  const [suggestions, setSuggestions] = useState([]);
  const [showSuggestions, setShowSuggestions] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    if (query.length > 1) {
      const filtered = SKILLS.filter(s => s.toLowerCase().includes(query.toLowerCase())).slice(0, 6);
      setSuggestions(filtered);
      setShowSuggestions(filtered.length > 0);
    } else {
      setSuggestions([]);
      setShowSuggestions(false);
    }
  }, [query]);

  const handleSearch = (e) => {
    e.preventDefault();
    const params = new URLSearchParams();
    if (query) params.set('q', query);
    if (location) params.set('location', location);
    navigate(`/jobs?${params.toString()}`);
  };

  const handleSuggestionClick = (skill) => {
    setQuery(skill);
    setShowSuggestions(false);
    navigate(`/jobs?q=${encodeURIComponent(skill)}`);
  };

  return (
    <div className="search-bar-container">
      <form className="search-bar" onSubmit={handleSearch}>
        <div className="search-input-group">
          <div className="search-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <circle cx="11" cy="11" r="8"></circle>
              <path d="M21 21l-4.35-4.35"></path>
            </svg>
          </div>
          <input
            type="text"
            placeholder="Job title, skills, company, or keyword..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            className="search-input"
            autoComplete="off"
          />
          {showSuggestions && (
            <div className="suggestions-dropdown">
              {suggestions.map((s, i) => (
                <div key={i} className="suggestion-item" onClick={() => handleSuggestionClick(s)}>
                  {s}
                </div>
              ))}
            </div>
          )}
        </div>
        <div className="location-input-group">
          <div className="location-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z"></path>
              <circle cx="12" cy="10" r="3"></circle>
            </svg>
          </div>
          <select
            value={location}
            onChange={(e) => setLocation(e.target.value)}
            className="location-select"
          >
            <option value="">All Locations</option>
            {LOCATIONS.map(loc => (
              <option key={loc} value={loc}>{loc}</option>
            ))}
          </select>
        </div>
        <button type="submit" className="search-button">
          Search Jobs
        </button>
      </form>
    </div>
  );
};

export default SearchBar;
