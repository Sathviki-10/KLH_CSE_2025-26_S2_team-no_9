import React from 'react';
import { Link } from 'react-router-dom';
import './JobCard.css';

const JobCard = ({ job }) => {
  const skillsToShow = job.skills ? job.skills.slice(0, 4) : [];
  const extraSkills = job.skills ? job.skills.length - 4 : 0;

  return (
    <div className="job-card">
      <div className="job-card-header">
        <div className="job-company-logo">
          {job.company ? job.company.charAt(0).toUpperCase() : 'C'}
        </div>
        <div className="job-card-title-group">
          <Link to={`/jobs/${job.id}`} className="job-title-link">
            <h3 className="job-title">{job.title}</h3>
          </Link>
          <p className="job-company">{job.company}</p>
        </div>
        <div className="job-relevance">
          <div className="relevance-score">
            {Math.round(job.relevance_score || 0)}%
          </div>
          <span className="relevance-label">Match</span>
        </div>
      </div>
      <div className="job-card-body">
        <div className="job-meta">
          <span className="job-meta-item">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z"></path>
              <circle cx="12" cy="10" r="3"></circle>
            </svg>
            {job.location}
          </span>
          <span className="job-meta-item">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <rect x="2" y="7" width="20" height="14" rx="2" ry="2"></rect>
              <path d="M16 21V5a2 2 0 00-2-2h-4a2 2 0 00-2 2v16"></path>
            </svg>
            {job.experience}
          </span>
          <span className="job-meta-item">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <circle cx="12" cy="12" r="10"></circle>
              <polyline points="12 6 12 12 16 14"></polyline>
            </svg>
            {job.job_type}
          </span>
        </div>
        <div className="job-salary">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <line x1="12" y1="1" x2="12" y2="23"></line>
            <path d="M17 5H9.5a3.5 3.5 0 000 7h5a3.5 3.5 0 010 7H6"></path>
          </svg>
          {job.salary}
        </div>
        <div className="job-skills">
          {skillsToShow.map((skill, i) => (
            <span key={i} className="skill-tag">{skill}</span>
          ))}
          {extraSkills > 0 && (
            <span className="skill-tag skill-more">+{extraSkills} more</span>
          )}
        </div>
      </div>
      <div className="job-card-footer">
        <Link to={`/jobs/${job.id}`} className="btn-view-job">
          View Details
        </Link>
      </div>
    </div>
  );
};

export default JobCard;
