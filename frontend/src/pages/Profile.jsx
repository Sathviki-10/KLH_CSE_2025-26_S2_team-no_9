import React, { useState, useEffect } from 'react';
import { getUser, getRecommendations, getSavedJobs } from '../services/api';
import { LOCATIONS, JOB_TYPES, SKILLS } from '../utils/constants';
import JobCard from '../components/JobCard';
import './Profile.css';

const Profile = ({ user }) => {
  const [profile, setProfile] = useState(null);
  const [recommendations, setRecommendations] = useState([]);
  const [savedJobs, setSavedJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('profile');

  useEffect(() => {
    const fetchData = async () => {
      if (!user) return;
      try {
        const profileData = await getUser(user.id);
        setProfile(profileData.user);
        const recs = await getRecommendations(user.id);
        setRecommendations(recs.recommendations || []);
        const saved = await getSavedJobs(user.id);
        setSavedJobs(saved.saved_jobs || []);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [user]);

  if (!user) {
    return (
      <div className="profile-login">
        <h2>Please sign in to view your profile</h2>
      </div>
    );
  }

  if (loading) {
    return (
      <div className="profile-loading">
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <div className="profile">
      <div className="profile-header">
        <div className="profile-avatar">
          {user.name ? user.name.charAt(0).toUpperCase() : 'U'}
        </div>
        <div className="profile-info">
          <h1>{user.name}</h1>
          <p>{user.email}</p>
        </div>
      </div>

      <div className="profile-tabs">
        <button
          className={`tab ${activeTab === 'profile' ? 'active' : ''}`}
          onClick={() => setActiveTab('profile')}
        >
          Profile
        </button>
        <button
          className={`tab ${activeTab === 'saved' ? 'active' : ''}`}
          onClick={() => setActiveTab('saved')}
        >
          Saved Jobs ({savedJobs.length})
        </button>
        <button
          className={`tab ${activeTab === 'recommended' ? 'active' : ''}`}
          onClick={() => setActiveTab('recommended')}
        >
          Recommended ({recommendations.length})
        </button>
      </div>

      <div className="profile-content">
        {activeTab === 'profile' && (
          <div className="profile-details">
            <div className="detail-card">
              <h3>Personal Information</h3>
              <div className="detail-grid">
                <div className="detail-item">
                  <label>Name</label>
                  <p>{profile?.name || user.name}</p>
                </div>
                <div className="detail-item">
                  <label>Email</label>
                  <p>{profile?.email || user.email}</p>
                </div>
                <div className="detail-item">
                  <label>Location</label>
                  <p>{profile?.location || user.location || 'Not set'}</p>
                </div>
                <div className="detail-item">
                  <label>Experience</label>
                  <p>{profile?.experience || user.experience || 'Not set'}</p>
                </div>
              </div>
            </div>
          </div>
        )}

        {activeTab === 'saved' && (
          <div className="saved-jobs">
            {savedJobs.length === 0 ? (
              <div className="empty-state">
                <div className="empty-icon">🔖</div>
                <h3>No saved jobs yet</h3>
                <p>Save jobs you're interested in to view them here</p>
              </div>
            ) : (
              <div className="job-grid">
                {savedJobs.map(job => (
                  <JobCard key={job.id} job={job} />
                ))}
              </div>
            )}
          </div>
        )}

        {activeTab === 'recommended' && (
          <div className="recommended-jobs">
            {recommendations.length === 0 ? (
              <div className="empty-state">
                <div className="empty-icon">💡</div>
                <h3>No recommendations yet</h3>
                <p>Complete your profile to get personalized job recommendations</p>
              </div>
            ) : (
              <div className="job-grid">
                {recommendations.map(job => (
                  <JobCard key={job.id} job={job} />
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default Profile;
