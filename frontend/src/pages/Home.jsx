import React from 'react';
import SearchBar from '../components/SearchBar';
import './Home.css';

const Home = () => {
  return (
    <div className="home">
      <section className="hero">
        <div className="hero-content">
          <h1 className="hero-title">
            Find Your <span className="gradient-text">Dream Job</span>
          </h1>
          <p className="hero-subtitle">
            Smart search powered by intelligent ranking. Discover relevant jobs from thousands of postings.
          </p>
          <div className="hero-search">
            <SearchBar />
          </div>
          <div className="hero-stats">
            <div className="stat">
              <span className="stat-number">10,000+</span>
              <span className="stat-label">Jobs</span>
            </div>
            <div className="stat">
              <span className="stat-number">5,000+</span>
              <span className="stat-label">Companies</span>
            </div>
            <div className="stat">
              <span className="stat-number">50+</span>
              <span className="stat-label">Skills</span>
            </div>
          </div>
        </div>
        <div className="hero-image">
          <div className="floating-card card-1">
            <div className="card-icon">💼</div>
            <div>
              <p className="card-title">Senior Developer</p>
              <p className="card-company">TechCorp</p>
            </div>
          </div>
          <div className="floating-card card-2">
            <div className="card-icon">🚀</div>
            <div>
              <p className="card-title">95% Match</p>
              <p className="card-company">Your Profile</p>
            </div>
          </div>
          <div className="floating-card card-3">
            <div className="card-icon">⭐</div>
            <div>
              <p className="card-title">Top Rated</p>
              <p className="card-company">Companies</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;
