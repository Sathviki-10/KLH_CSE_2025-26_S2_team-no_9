import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getJob, getJobs } from '../services/api';
import JobCard from './JobCard';
import './JobDetails.css';

const JobDetails = ({ user, onSaveJob, onUnsaveJob, savedJobIds = [] }) => {
  const { id } = useParams();
  const [job, setJob] = useState(null);
  const [similarJobs, setSimilarJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchJob = async () => {
      try {
        setLoading(true);
      const data = await getJob(id);
      setJob(data);
      setError(null);
      } catch (err) {
        setError('Failed to load job details');
      } finally {
        setLoading(false);
      }
    };
    fetchJob();
  }, [id]);

  useEffect(() => {
    const fetchSimilar = async () => {
      if (!job) return;
      try {
        const data = await getJobs(1, 5);
        const filtered = data.jobs.filter(j => j.id !== job.id).slice(0, 4);
        setSimilarJobs(filtered);
      } catch (err) {
        console.error(err);
      }
    };
    fetchSimilar();
  }, [job]);

  if (loading) {
    return (
      <div className="job-details-loading">
        <div className="spinner"></div>
        <p>Loading job details...</p>
      </div>
    );
  }

  if (error || !job) {
    return (
      <div className="job-details-error">
        <h2>Job Not Found</h2>
        <p>{error || 'The job you are looking for does not exist.'}</p>
        <Link to="/jobs" className="btn-back">Browse Jobs</Link>
      </div>
    );
  }

  const isSaved = savedJobIds.includes(parseInt(id));

  return (
    <div className="job-details">
      <div className="job-details-header">
        <div className="job-details-title-group">
          <div className="job-details-logo">
            {job.company ? job.company.charAt(0).toUpperCase() : 'C'}
          </div>
          <div>
            <h1 className="job-details-title">{job.title}</h1>
            <p className="job-details-company">{job.company}</p>
          </div>
        </div>
        <div className="job-details-actions">
          {user && (
            <button
              className={`btn-save ${isSaved ? 'saved' : ''}`}
              onClick={() => isSaved ? onUnsaveJob(user.id, job.id) : onSaveJob(user.id, job.id)}
            >
              {isSaved ? 'Saved' : 'Save Job'}
            </button>
          )}
          <button className="btn-apply">Apply Now</button>
        </div>
      </div>

      <div className="job-details-meta">
        <div className="meta-card">
          <div className="meta-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z"></path>
              <circle cx="12" cy="10" r="3"></circle>
            </svg>
          </div>
          <div>
            <p className="meta-label">Location</p>
            <p className="meta-value">{job.location}</p>
          </div>
        </div>
        <div className="meta-card">
          <div className="meta-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <rect x="2" y="7" width="20" height="14" rx="2" ry="2"></rect>
              <path d="M16 21V5a2 2 0 00-2-2h-4a2 2 0 00-2 2v16"></path>
            </svg>
          </div>
          <div>
            <p className="meta-label">Experience</p>
            <p className="meta-value">{job.experience}</p>
          </div>
        </div>
        <div className="meta-card">
          <div className="meta-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <line x1="12" y1="1" x2="12" y2="23"></line>
              <path d="M17 5H9.5a3.5 3.5 0 000 7h5a3.5 3.5 0 010 7H6"></path>
            </svg>
          </div>
          <div>
            <p className="meta-label">Salary</p>
            <p className="meta-value">{job.salary}</p>
          </div>
        </div>
        <div className="meta-card">
          <div className="meta-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <circle cx="12" cy="12" r="10"></circle>
              <polyline points="12 6 12 12 16 14"></polyline>
            </svg>
          </div>
          <div>
            <p className="meta-label">Job Type</p>
            <p className="meta-value">{job.job_type}</p>
          </div>
        </div>
      </div>

      <div className="job-details-section">
        <h2>Job Description</h2>
        <p className="job-description">{job.description}</p>
      </div>

      <div className="job-details-section">
        <h2>Required Skills</h2>
        <div className="job-details-skills">
          {(job.skills || []).map((skill, i) => (
            <span key={i} className="skill-tag-large">{skill}</span>
          ))}
        </div>
      </div>

      {similarJobs.length > 0 && (
        <div className="job-details-section">
          <h2>Similar Jobs</h2>
          <div className="similar-jobs-grid">
            {similarJobs.map(j => (
              <JobCard key={j.id} job={j} />
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default JobDetails;
